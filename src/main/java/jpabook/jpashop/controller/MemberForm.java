package jpabook.jpashop.controller;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {


    @NotEmpty(message = "회원 이름은 필수 입니다")
    public String name;

    public String city;
    public String street;
    public String zipcode;

}
