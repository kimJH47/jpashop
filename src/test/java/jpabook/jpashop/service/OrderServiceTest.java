package jpabook.jpashop.service;


import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;


@SpringBootTest
@Transactional
public class OrderServiceTest {
    

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 주문생성() throws Exception{
        //given
        Member member = createMember();

        Book book = createBook("책", 10000, 10);
        int orderCount = 2;
        //when
        Long id = orderService.order(member.getId(), book.getId(), 2);

        //then

        Order one = orderRepository.findOne(id);
        assertThat(one.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(one.getOrderItems().size()).isEqualTo(1);
        List<OrderItem> orderItems = one.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            assertThat(orderItem.getCount()).isEqualTo(2);
        }
        assertThat(one.getTotalPrice()).isEqualTo(20000);
        assertThat(one.getTotalPrice()).isEqualTo(20000);
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }



    @Test
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("책", 10000, 10);
        int orderCount = 11;

        //when

        //then

        org.junit.jupiter.api.Assertions.assertThrows(NotEnoughStockException.class,
        ()->orderService.order(member.getId(), book.getId(), orderCount),"예외발생");


    }
    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Book item = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}
