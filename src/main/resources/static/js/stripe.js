// Stripe 公開鍵
const stripe = Stripe("pk_test_51SozlyLsLHjFZW90yfTZSBfCFG4KW9UIa4nGpBuwRX9wYydFYtqTNvbhnNLRIOwMBLBN2BD4tgOwsQIHHXCUamJb00dS0YRgzo");

document.addEventListener("DOMContentLoaded", () => {
  const checkoutBtn = document.getElementById("checkout-btn");
  const form = document.getElementById("checkout-form");
  if (!checkoutBtn || !form) {
    console.error("checkout button or form not found");
    return;
  }

  checkoutBtn.addEventListener("click", () => {

    // 二重クリック防止
    checkoutBtn.disabled = true;

    const formData = new FormData(form);
    const jsonData = Object.fromEntries(formData.entries());
    console.log("実際に送るデータ:", jsonData); // ← これをチェック！
    fetch("/api/stripe/checkout", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(jsonData)
    })
      .then(res => {
        if (!res.ok) {
          throw new Error("checkout api error");
        }
        return res.json();
      })
      .then(data => {
        return stripe.redirectToCheckout({
          sessionId: data.sessionId
        });
      })
      .then(result => {
        // Stripe側でエラーが返ってきた場合
        if (result.error) {
          console.error(result.error);
          alert(result.error.message);
          checkoutBtn.disabled = false;
        }
      })
      .catch(err => {
        // 通信エラー・想定外エラー
        console.error(err);
        alert("決済処理に失敗しました。もう一度お試しください。");
        checkoutBtn.disabled = false;
      });
      });
      });
