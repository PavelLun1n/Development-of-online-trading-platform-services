    package com.example.restController;

    import com.example.entity.enums.EventType;
    import com.example.model.User;
    import com.example.model.UserAction;
    import com.example.service.UserService;
    import com.example.service.UserActionService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.MediaType;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.view.RedirectView;

    import java.time.LocalDateTime;
    import java.util.List;

    @RestController
    @RequestMapping(path = "/api/users", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public class UserController {

        private final UserService userService;
        private final UserActionService userActionService;

        @Autowired
        public UserController(UserService userService, UserActionService userActionService) {
            this.userService = userService;
            this.userActionService = userActionService;
        }

        @PostMapping("/register")
        public User register(@RequestBody User user) {
            return userService.register(user);

        }

        @PostMapping("/login")
        public RedirectView login(@RequestBody User user) {
            User authenticatedUser = userService.authenticate(user);

            // Логируем событие "вход в систему"
            UserAction loginAction = UserAction.builder()
                    .eventTime(LocalDateTime.now())
                    .eventType(EventType.view) // Или введи новый тип "login"
                    .productId(0L) // Не к товару относится
                    .userId(authenticatedUser.getId())
                    .userSession("session-" + authenticatedUser.getId()) // Или реальную UUID-сессию
                    .build();

            userActionService.saveAction(loginAction);

            return new RedirectView("http://localhost:8080");
        }


        @GetMapping("/{id}")
        public User getUserProfile(@PathVariable Long id) {
            return userService.getUserById(id);
        }

        @PutMapping("/{id}")
        public User updateProfile(@PathVariable Long id, @RequestBody User updatedUser) {
            return userService.updateUser(id, updatedUser);
        }

        @GetMapping("/{id}/orders")
        public List<?> getUserOrders(@PathVariable Long id) {
            return userService.getOrdersByUserId(id);
        }

        @GetMapping("/{id}/bids")
        public List<?> getUserBids(@PathVariable Long id) {
            return userService.getBidsByUserId(id);
        }
    }
