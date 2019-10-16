package firstClass.body.rest;

import firstClass.body.entity.User;
import firstClass.body.service.UserService;
import firstClass.body.service.request.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: wkit
 * @Date: 2019-10-15 17:23
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/blog/user/search")
    public ResponseEntity<List<User>> searchUserList(@RequestParam String userName, PageRequest pageRequest) {
        return ResponseEntity.ok(userService.searchUserList(userName, pageRequest));
    }
}
