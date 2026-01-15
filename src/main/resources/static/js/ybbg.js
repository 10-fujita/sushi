
// 3. 登録済み住所のプルダウン切り替えイベント
$('#saved-address-selector').on('change', function() {
    const selectedOption = $(this).find('option:selected');
    
    // データの取得
    const zip = selectedOption.data('zip');
    const pref = selectedOption.data('pref');
    const city = selectedOption.data('city');
    const line1 = selectedOption.data('line1');
    const line2 = selectedOption.data('line2');

    if (zip) {
        // ① 見た目の表示を更新
        $('#display-zip').text('〒' + zip);
        $('#display-addr').text(pref + city + line1 + ' ' + (line2 || ''));

        // ② ★重要：サーバーに送る「入力欄」に値をコピーする
        // これをしないと、確認画面には「前に入力されていた値」が飛んでしまいます
        $('#ybbg').val(zip);      // 郵便番号入力欄
        $('#tdhnm').val(pref);    // 都道府県入力欄
        $('#adr1').val(city);     // 市区町村入力欄
        $('#adr2').val(line1);    // 番地入力欄
        // 建物名のidがHTMLで空だったので、もしid="adr3"など付けていればセット
        // $('input[th\\:field="*{addressLine2}"]').val(line2); 
    }
});
function filterOrders(status) {
    // 1. ボタンの色の切り替え
    const buttons = document.querySelectorAll('.filter-btn');
    buttons.forEach(btn => {
        btn.classList.remove('active');

        // ボタンの onclick に書いた引数と一致するかチェック
        // ※ buttonタグ自体の属性から判定するのが確実です
        if (btn.getAttribute('onclick').includes(`'${status}'`)) {
            btn.classList.add('active');
        }
    });

    // 2. テーブルの行を絞り込み
    const rows = document.querySelectorAll('.admin-table tbody tr');

    rows.forEach(row => {
        const statusCell = row.querySelector('.status-cell');
        if (!statusCell) return;

        // ここで DB の値（ORDERED / COMPLETED）を取得
        const orderStatus = statusCell.innerText.trim();

        if (status === 'all') {
            row.style.display = '';
        } else if (orderStatus === status) {
            row.style.display = ''; // DBの英単語と引数が一致
        } else {
            row.style.display = 'none';
        }
    });
}
function updateTimeSlots() {
    // もし currentHour が定義されていなければ何もしない（エラー防止）
    if (typeof currentHour === 'undefined') {
        return;
    }

    const dateSelect = document.getElementById('deliveryDate');
    const timeSelect = document.getElementById('deliveryTimeSlot');

    if (!dateSelect || !timeSelect) return;

    const selectedDate = dateSelect.value;

    // 今日の日付を取得
    const now = new Date();
    const y = now.getFullYear();
    const m = String(now.getMonth() + 1).padStart(2, '0');
    const d = String(now.getDate()).padStart(2, '0');

    // スラッシュ形式とハイフン形式の両方を用意
    const todaySlash = `${y}/${m}/${d}`;
    const todayHyphen = `${y}-${m}-${d}`;

    const limitHour = currentHour + 3;
    const options = timeSelect.options;

    for (let i = 1; i < options.length; i++) {
        const slotHour = parseInt(options[i].value.split(':')[0]);

        // selectedDateが「今日（スラッシュまたはハイフン）」と一致するか判定
        if (selectedDate === todaySlash || selectedDate === todayHyphen) {
            if (slotHour < limitHour) {
                options[i].style.display = 'none';
            } else {
                options[i].style.display = 'block';
            }
        } else {
            // 今日以外（明日以降）なら全て表示
            options[i].style.display = 'block';
        }
    }

    // 隠された時間が選択されていたらリセット
    if (timeSelect.selectedIndex !== -1 && options[timeSelect.selectedIndex].style.display === 'none') {
        timeSelect.value = "";
    }
}
/* 郵便番号API */
const YBBG_URL = 'https://zipcloud.ibsnet.co.jp/api/search';

// 郵便番号チェック（ハイフンなし7桁）
function isZcode(zcode) {
    return /^[0-9]{7}$/.test(zcode);
}

// 郵便番号から住所取得
function getAddrByYbbg(ybbg) {
    const zipcode = ybbg.replace(/[^\d]/g, '');
    const url = `${YBBG_URL}?zipcode=${zipcode}`;

    fetch(url)
        .then(res => res.json())
        .then(data => showAddrByYbbg(data))
        .catch(err => console.error(err));
}

// 住所反映
// 住所反映 (複数候補対応版)
function showAddrByYbbg(data) {
    const $tdhnm = $('#tdhnm');
    const $adr1 = $('#adr1');
    const $error = $('#ybbgError');
    
    // 以前の選択リストがあれば削除
    $('#address-selector').remove();

    if (!data.results) {
        $error.text('該当する住所が存在しません。');
        return;
    }

    if (data.results.length === 1) {
        // 1件なら自動入力
        const result = data.results[0];
        $tdhnm.val(result.address1);
        $adr1.val(result.address2 + result.address3);
        $error.text('');
    } else if (data.results.length > 1) {
        // 複数あれば選択肢を表示
        $error.text('住所が複数見つかりました。選択してください。');
        
        // デザインを崩さないよう、セレクトボックスを作成
        let selectHtml = '<select id="address-selector" style="width:100%; padding:8px; margin-top:5px; border:1px solid #ccc; border-radius:4px;">';
        selectHtml += '<option value="">--- 住所を選択してください ---</option>';
        
        data.results.forEach((res, index) => {
            const fullAddr23 = res.address2 + res.address3;
            selectHtml += `<option value="${index}" data-pref="${res.address1}" data-city="${fullAddr23}">${res.address1}${fullAddr23}</option>`;
        });
        selectHtml += '</select>';

        // 郵便番号のinputの後ろ（errorスパンの前）に挿入
        $('#ybbg').after(selectHtml);

        // 選択した時のイベント
        $('#address-selector').on('change', function() {
            const $opt = $(this).find('option:selected');
            if ($opt.val() !== "") {
                $tdhnm.val($opt.data('pref'));
                $adr1.val($opt.data('city'));
                $error.text('');
                $(this).remove(); // 選択が終わったら消す
            }
        });
    }
}

// --- ページ読み込み完了時に一括実行 ---
$(document).ready(function() {

    // 1. 郵便番号検索のイベント
    $(document).on('focusout', '#ybbg', function() {
        const ybbg = $(this).val().trim();
        if (isZcode(ybbg)) {
            getAddrByYbbg(ybbg);
        } else if (ybbg !== '') {
            $('#ybbgError').text('郵便番号は7桁の数字で入力してください。');
        }
    });

    // 2. お届け先選択による表示・非表示制御
    const addressRadios = document.querySelectorAll('input[name="addressSelect"]');
    const newAddressArea = document.getElementById('new-address-form-area');
    const placeholder = document.getElementById('new-address-placeholder');

    const toggleNewAddressForm = () => {
        // 現在チェックされているラジオボタンの「値」を取得
        const checkedRadio = document.querySelector('input[name="addressSelect"]:checked');
        const selectedValue = checkedRadio ? checkedRadio.value : null;

        if (selectedValue === "3") {
            // 「新しく入力する」が選ばれた時だけフォームを表示
            if (newAddressArea) newAddressArea.style.display = 'block';
            if (placeholder) placeholder.style.display = 'none';
        } else {
            // それ以外（1番目、2番目）ならフォームを隠す
            if (newAddressArea) newAddressArea.style.display = 'none';
            if (placeholder) placeholder.style.display = 'block';
        }
    };

    // すべてのラジオボタンにイベントを登録
    addressRadios.forEach(radio => {
        radio.addEventListener('change', toggleNewAddressForm);
    });

    // 初期実行（画面を開いた瞬間の状態を反映）
    toggleNewAddressForm();
    // お届け日時の制御を起動
    const dateSelect = document.getElementById('deliveryDate');
    if (dateSelect) {
        // イベント登録
        dateSelect.addEventListener('change', updateTimeSlots);
        // 初期実行
        updateTimeSlots();
    }
    // 3. 登録済み住所のプルダウン切り替えイベント ★ここを追記
    $('#saved-address-selector').on('change', function() {
        // 選択された <option> からカスタム属性（data-...）を使って値を取得
        const selectedOption = $(this).find('option:selected');
        const zip = selectedOption.data('zip');
        const addr = selectedOption.data('addr');

        if (zip) {
            $('#display-zip').text('〒' + zip);
            $('#display-addr').text(addr);

            // ラジオボタン「1（登録住所）」を自動でチェック状態にする
            const radio1 = document.getElementById('radio1');
            if (radio1) {
                radio1.checked = true;
                // ラジオボタンが変わったことを通知（フォームの表示/非表示制御を動かすため）
                radio1.dispatchEvent(new Event('change'));
            }
        }
    });

    // 初期実行（もし最初からプルダウンがある場合は反映させる）
    //if ($('#saved-address-selector').length) {
       // $('#saved-address-selector').trigger('change');
   // }
});