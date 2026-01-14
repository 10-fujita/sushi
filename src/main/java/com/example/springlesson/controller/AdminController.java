package com.example.springlesson.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.springlesson.entity.Order;
import com.example.springlesson.entity.Product;
import com.example.springlesson.entity.User;
import com.example.springlesson.form.AdminAddProductForm;
import com.example.springlesson.form.AdminCustomerForm;
import com.example.springlesson.form.AdminOrderForm;
import com.example.springlesson.form.AdminProductForm;
import com.example.springlesson.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {


  private final AdminService adminService;

  public AdminController( AdminService adminService) {
    this.adminService = adminService;
   
  }
  
  @GetMapping("/login")
  public String login() {
    return "admin/login";
  }


@GetMapping("/adminPage")
public String adminPage() {
   // templates/admin/adminPage.html を表示する
   return "admin/adminPage";
}
  @GetMapping("/userManagement")
  public String userManagement(Model model) {
    try {
      List<User> userList = adminService.findAllUsers();
      model.addAttribute("userList", userList);
      
      model.addAttribute("AdminCustomerForm", new AdminCustomerForm());
      return "admin/adminUser";
    } catch (Exception e) {
      model.addAttribute("errMsg", "ユーザー情報の取得中にエラーが発生しました。");
      return "error/error";
    }
  }

  @PostMapping("/checkUser")
  public String checkUser( @Valid @ModelAttribute("AdminCustomerForm") AdminCustomerForm form,
      BindingResult bindingResult,
      HttpSession session,
      Model model) {
    if (bindingResult.hasErrors()) {
      //エラー時の処理
      return "admin/userManagement";
    }
    try {
      List<User> userList = adminService.findByIdIn(form.getUserIds());
      session.setAttribute("userCheckList", userList);
      model.addAttribute("userList", userList);
      return "admin/adminUserCheck";
    } catch (Exception e) {
      model.addAttribute("errMsg", "ユーザー情報の取得中にエラーが発生しました。");
      return "error/error";
    }
  }

  @PostMapping("/disableUser")
  public String disableUser(HttpSession session, Model model) {
    try {

      List<User> userList = (List<User>) session.getAttribute("userCheckList");
      for (User user : userList) {
        adminService.disableUser(user.getId());
      }
      session.removeAttribute("userCheckList");
      return "admin/finish";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "ユーザーの無効化中にエラーが発生しました。");
      return "error/error";
    }
  }
  @GetMapping("/productManagement")
  public String productManagement(Model model) {
    try {
      List<Product> productList = adminService.findAllProducts();
      model.addAttribute("productList", productList);
      model.addAttribute("AdminProductForm", new AdminProductForm());
      return "admin/adminProduct";
    } catch (Exception e) {
      model.addAttribute("errMsg", "商品情報の取得中にエラーが発生しました。");
      return "error/error";
    }
  }
  @PostMapping("/checkProduct")
  public String checkProduct( @Valid @ModelAttribute("AdminProductForm") AdminProductForm form,
      BindingResult bindingResult,
      HttpSession session,
      Model model) {
    if (bindingResult.hasErrors()) {
      //エラー時の処理
      return "admin/adminProduct";
    }
    try {
      List<Product> productList = adminService.findByIdInProducts(form.getProductIds());
      session.setAttribute("productCheckList", productList);
      model.addAttribute("productList", productList);
      return "admin/adminProductCheck";
    } catch (Exception e) {
      model.addAttribute("errMsg", "商品情報の取得中にエラーが発生しました。");
      return "error/error";
    }
  }
  @PostMapping("/disableProduct")
  public String disableProduct(HttpSession session, Model model) {
    try {

      List<Product> productList = (List<Product>) session.getAttribute("productCheckList");
      for (Product product : productList) {
        adminService.disableProduct(product.getId());
      }
      session.removeAttribute("productCheckList");
      return "admin/finish";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "商品の無効化中にエラーが発生しました。");
      return "error/error";
    }
  }
  @GetMapping("/adminAddProduct")
  public String adminaddProduct(Model model) {
    model.addAttribute("AdminAddProductForm", new AdminAddProductForm());
    return "admin/adminAddProduct";
  }
  @PostMapping("/adminAddProduct")
  public String adminAddProduct( @Valid @ModelAttribute("AdminAddProductForm") AdminAddProductForm form,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      //エラー時の処理
   // コンソールにエラー内容をすべて表示する（これで原因がわかります）
      bindingResult.getAllErrors().forEach(error -> {
          System.out.println("Validation Error: " + error.toString());
      });
      return "admin/adminAddProduct";
    }
    try {
     
     Product savedProduct =  adminService.saveProduct(form);
     
  // 画像を保存 
     MultipartFile image = form.getImage();
     if (image != null && !image.isEmpty()) {
         String uploadDir = new File("src/main/resources/static/images/products").getAbsolutePath();
         File folder = new File(uploadDir);
         if (!folder.exists()) folder.mkdirs();

         // 商品IDでファイル名を固定
         String fileName = savedProduct.getId() + ".png";
         Path path = Paths.get(uploadDir, fileName);
         Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

         // 保存した画像名をProductに設定
      // 文字列を結合してセット
         String fullPathForDb = "/images/products/" + fileName;
         savedProduct.setImageUrl(fullPathForDb);
         adminService.updateProduct(savedProduct); // 画像URLを更新
    
     }
    }catch (Exception e) {
      model.addAttribute("errMsg", "商品の追加中にエラーが発生しました。");
      return "error/error";
    }
    return "admin/adminPage";
  }
  @GetMapping("/orderManagement")
  public String orderManagement(Model model) {
    try {
      List<Order> orderList = adminService.findAllOrders();
      model.addAttribute("orderList", orderList);
      
    }catch (Exception e) {
      model.addAttribute("errMsg", "注文情報の取得中にエラーが発生しました。");
      return "error/error";
    }
    return "admin/adminOrder";
  }
  @PostMapping("/checkOrder")
  public String checkOrder( @Valid @ModelAttribute("AdminOrderForm") AdminOrderForm
      form,
      BindingResult bindingResult,
      HttpSession session,
      Model model) {
    if (bindingResult.hasErrors()) {
      //エラー時の処理
      return "admin/adminOrder";
    }
    try {
      List<Order> orderList = adminService.findByOrderId(form.getOrderIds());
      session.setAttribute("orderCheckIds", form.getOrderIds());
      model.addAttribute("orderCheckList", orderList);
      return "admin/adminOrderCheck";
    } catch (Exception e) {
      model.addAttribute("errMsg", "注文情報の取得中にエラーが発生しました。");
      return "error/error";
    }
  }
  @PostMapping("/completeOrder")
  public String completeOrder(HttpSession session, Model model) {
    try {

      List<Long> fOrderIds = (List<Long>) session.getAttribute("orderCheckIds");
      adminService.save(fOrderIds);
      session.removeAttribute("orderCheckIds");
      return "admin/finish";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "注文の完了処理中にエラーが発生しました。");
      return "error/error";
    }
  }
}
