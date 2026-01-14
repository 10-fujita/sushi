const cart = {};

document.addEventListener('DOMContentLoaded', () => {
  
  // 1. 初期データの同期処理
  // ------------------------------------------
  document.querySelectorAll('.product_card').forEach(card => {
    const productId = card.dataset.productId;
    const qty = parseInt(card.querySelector('.qty_num').textContent) || 0;
    if (qty > 0) {
      cart[productId] = qty;
    }
  });

  // 初期表示時の合計金額を計算
  updateTotal();

  // 2. ＋ボタンの処理
  // ------------------------------------------
  document.querySelectorAll('.qty_btn.plus').forEach(btn => {
    btn.addEventListener('click', e => {
      const card = e.currentTarget.closest('.product_card');
      const productId = card.dataset.productId;
      cart[productId] = (Number(cart[productId]) || 0) + 1;
      updateCard(card, cart[productId]);
      updateTotal();
    });
  });

  // 3. －ボタンの処理
  // ------------------------------------------
  document.querySelectorAll('.qty_btn.minus').forEach(btn => {
    btn.addEventListener('click', e => {
      const card = e.currentTarget.closest('.product_card');
      const productId = card.dataset.productId;
      if (!cart[productId] || cart[productId] <= 0) return;
      cart[productId] = Number(cart[productId]) - 1;
      updateCard(card, cart[productId]);
      updateTotal();
    });
  });

  // 4. 送信時の処理 (hidden input生成)
  // ------------------------------------------
  // 4. 送信時の処理 (hidden input生成)
    const cartForm = document.getElementById('cartForm');
    if (cartForm) {
      cartForm.addEventListener('submit', function(e) {
        const container = document.getElementById('hiddenInputs');
        if (!container) return;
        
        container.innerHTML = ''; 

        // 画面上のすべての商品カードをループして、現在の数をすべて送る
        document.querySelectorAll('.product_card').forEach(card => {
          const productId = card.dataset.productId;
          const qty = cart[productId] || 0; // 0でも取得する

          const idInput = document.createElement('input');
          idInput.type = 'hidden';
          idInput.name = 'productIds';
          idInput.value = productId;
          
          const qtyInput = document.createElement('input');
          qtyInput.type = 'hidden';
          qtyInput.name = 'quantities';
          qtyInput.value = qty;

          container.appendChild(idInput);
          container.appendChild(qtyInput);
        });

        // すべての商品が0なら送信を止める（任意）
        const hasItems = Object.values(cart).some(v => v > 0);
        if (!hasItems) {
          alert('商品を選択してください');
          e.preventDefault();
        }
      });
    }
  // 5. カテゴリーフィルター処理
  // ------------------------------------------
  const categorySelect = document.querySelector('select[name="categoryId"]');
  if (categorySelect) {
    categorySelect.addEventListener('change', (e) => {
      const selectedId = e.target.value; 
      const cards = document.querySelectorAll('.product_card');

      cards.forEach(card => {
        const cardCategoryId = card.dataset.categoryId;
        // 未選択(すべて表示)の場合は空文字や特定の値を想定
        if (selectedId === "" || cardCategoryId === selectedId) {
          card.style.display = ""; 
        } else {
          card.style.display = "none"; 
        }
      });
    });
  }

}); // DOMContentLoaded の閉じカッコはここ1回だけ！

// --- 以下、補助関数（イベントリスナーの外側でOK） ---

function updateCard(card, qty) {
  const qtyNum = card.querySelector('.qty_num');
  if (qtyNum) qtyNum.textContent = qty;
}

function updateTotal() {
  let totalQty = 0;
  let totalPrice = 0;

  document.querySelectorAll('.product_card').forEach(card => {
    const productId = card.dataset.productId;
    const price = Number(card.dataset.price) || 0;
    const qty = cart[productId] || 0;

    totalQty += qty;
    totalPrice += qty * price;
  });

  const totalQtyEl = document.getElementById('totalQty');
  const totalPriceEl = document.getElementById('totalPrice');

  if (totalQtyEl) totalQtyEl.textContent = totalQty;
  if (totalPriceEl) totalPriceEl.textContent = totalPrice.toLocaleString();
}