package jpabook.jpashop.controller;



import jpabook.jpashop.domain.item.Book;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class itemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createFrom(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());


        itemService.save(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        model.addAttribute("items", itemService.findItems());
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId")Long itemId , Model model) {
        Book item =(Book) itemService.findOne(itemId);
        BookForm bookForm = new BookForm();

        bookForm.setId(item.getId());
        bookForm.setAuthor(item.getAuthor());
        bookForm.setIsbn(item.getIsbn());
        bookForm.setPrice(item.getPrice());
        bookForm.setName(item.getName());
        bookForm.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", bookForm);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId")Long itemId ,@ModelAttribute("form") BookForm bookForm) {
        itemService.updateItem(bookForm.getId(), bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity());
        return "redirect:/items";

    }

}
