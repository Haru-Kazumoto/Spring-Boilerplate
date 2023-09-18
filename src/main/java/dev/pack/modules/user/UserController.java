package dev.pack.modules.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pack.dtos.request.RequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dev.pack.payloads.response.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v${application.version}/public/admin")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @GetMapping(path = "/get/{size}/{page}")
    public ResponseEntity<?> findAllWithPaging(@PathVariable("size") int size, @PathVariable("page") int page){
        Pageable pageable = PageRequest.of(page,size);
        Iterable<UserModel> users = userService.getAllUser(pageable);
        return ResponseEntity.status(200).body(
                new ApiResponse(
                        200,
                        new Date(),
                        users
                )
        );
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid RequestDto.UserCreateDto bodyDto){
        UserModel user = modelMapper.map(bodyDto, UserModel.class);
        UserModel result = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        200,
                        new Date(),
                        result
                )
        );
    }
}
