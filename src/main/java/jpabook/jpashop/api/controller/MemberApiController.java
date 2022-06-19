package jpabook.jpashop.api.controller;


import jpabook.jpashop.controller.MemberForm;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long join = memberService.join(member);
        return new CreateMemberResponse(join);
    }

    @GetMapping("api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
    @GetMapping("api/v2/members")
    public Result membersV2() {
        List<MemberDto> collect = memberService.findMembers()
                                               .stream()
                                               .map(member -> new MemberDto(member.getName()))
                                               .collect(Collectors.toList());

        return new Result(collect);
    }
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest createMemberRequest) {

        Member member = new Member();
        member.setName(createMemberRequest.getName());
        Long join = memberService.join(member);

        return new CreateMemberResponse(join);
    }
    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id")Long id,
            @RequestBody @Valid UpdateMemberRequest updateMemberRequest) {

        memberService.update(id, updateMemberRequest.getName());
        Member one = memberService.findOne(id);
        return new UpdateMemberResponse(one.getId(), one.getName());

    }


    @Data
    static class  UpdateMemberRequest{
        private String name;

    }
    @Data
    static class  CreateMemberRequest{
        private String name;

    }
    @Data
    static class  CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long join) {
            this.id = join;
        }
    }

    @Data
    @AllArgsConstructor
    static class  UpdateMemberResponse{
        private Long id;
        private String name;

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;

    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }


}