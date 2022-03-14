
package jpabook.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Embeddable
@Getter @Setter
public class Address {
        private String city;
        private String street;
        private String zipcode;

        protected Address(){

        }//jpa 스펙상으로 기본생성자 protected 사용
        public Address(String city, String street, String zipcode) {
                this.city = city;
                this.street = street;
                this.zipcode = zipcode;
        }
}
