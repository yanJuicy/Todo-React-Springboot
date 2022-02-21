package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {

	@Autowired
	private TodoService service;

	@GetMapping("/test")
	public ResponseEntity<?> testTodo() {
		String str = service.testService(); // 테스트 서비스 사용
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}

	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
		try {
			String temporaryUserId = "temporary-user"; // temporary user id

			// 1 TodoEntity로 변환
			TodoEntity entity = TodoDTO.toEntity(dto);

			// 2 id를 null로 초기화
			entity.setId(null);

			// 3 임시 사용자 아이디 설정
			entity.setUserId(temporaryUserId);

			// 4 서비스를 이용해 Todo 엔터티 생성
			List<TodoEntity> entities = service.craete(entity);

			// 5 자바 스트림을 이용해 리턴된 리스트를 Todo로 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

			// 6 변환된 TodoDto 리스트를 이용해 ResponseDto를 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			// 예외가 있는 경우 dto 대신 error에 메시지를 넣어 리턴
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping
	public ResponseEntity<?> retrieveTodoList() {
		String temporaryUserId = "temporary-user";

		// 1 서비스 메서드의 retrieve() 메서드를 사용해 Todo 리스트를 가져온다.
		List<TodoEntity> entities = service.retrieve(temporaryUserId);

		// 2 자바 스트림을 이용해 리턴된 엔터티 리스트를 TodoDto 리스트로 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

		// 3 변환된 TodoDto 리스트를 이용해 ResponseDto를 초기화한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

		// 4 ResponseDto를 리턴
		return ResponseEntity.ok().body(response);
	}

	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
		String temporaryUserId = "temporary-user";

		// 1 dto를 entity로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);

		// 2 id를 temporaryUserId로 초기화
		entity.setUserId(temporaryUserId);

		// 3 서비스를 이용해 entity를 업데이트
		List<TodoEntity> entities = service.update(entity);

		// 4 자바 스트릠을 이용해 리턴된 엔터ㅣ 리스트를 TodoDto 리스트로 변환
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

		// 5 변환된 TodoDto 리스트를 이용해 ResponseDto를 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

		// 6 ResponseDto를 리턴
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
		try {
			String temporaryUserId = "temporary-user";

			// TodoEntity 변환
			TodoEntity entity = TodoDTO.toEntity(dto);

			// 임시 사용자 아이디 설정
			entity.setUserId(temporaryUserId);

			// 서비스를 이용해 entity 삭제
			List<TodoEntity> entities = service.delete(entity);

			// 자바 스트림을 이용해 리턴된 엔터티 리스트를 TodoDto 리스트 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

			// 변환된 TodoDto 리스트를 이용해 ResponseDto 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(error);
		}
	}

}
