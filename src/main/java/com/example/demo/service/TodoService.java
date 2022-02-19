package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TodoService {

	@Autowired
	private TodoRepository repository;

	public String testService() {
		// TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();

		// TodoEntity 저장
		repository.save(entity);

		// TodoEntity 검색
		TodoEntity savedEntity = repository.findById(entity.getId()).get();

		return savedEntity.getTitle();
	}

	public List<TodoEntity> craete(final TodoEntity entity) {
		// validation
		validate(entity);

		repository.save(entity);

		log.info("Entity Id: {} is saved", entity.getUserId());

		return repository.findByUserId(entity.getUserId());
	}

	private void validate(final TodoEntity entity) {
		if (entity == null) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null");
		}

		if (entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unkonwn user.");
		}
	}

	public List<TodoEntity> retrieve(final String userId) {
		return repository.findByUserId(userId);
	}

	public List<TodoEntity> update(final TodoEntity entity) {
		// 1 저장할 엔터티가 유효한지 확인.
		validate(entity);

		// 2 넘겨받은 엔터티 id를 이용해 TodoEntity를 가져온다.
		final Optional<TodoEntity> original = repository.findById(entity.getId());

		original.ifPresent(todo -> {
			// 3 반환된 TodoEntity가 존재하지 않으면 새 entity 값으로 덮어 씌운다.
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());

			// 4 데이터베이스에 새 값을 저장
			repository.save(todo);
		});

		// Retrieve Todo에서 만든 메서드를 이용해 사용자의 모든 Todo 리스트를 리턴한다.
		return retrieve(entity.getUserId());
	}

	public List<TodoEntity> delete(final TodoEntity entity) {
		// 엔터티 유효 호가인
		validate(entity);

		try {
			// 엔터티 삭제
			repository.delete(entity);
		} catch (Exception e) {
			// exception 발생 시 id와 excpetion을 로깅
			log.error("error deleteing entity", entity.getId(), e);

			// 컨트롤러로 excpetion을 보낸다 DB 내부 로직을 캡슐화하려면 e를 리턴하지 않고 새 excpetio 오브젝트 리턴
			throw new RuntimeException("error deleting entity " + entity.getId());
		}

		// 새 Todo 리스트를 가져와 리턴
		return retrieve(entity.getUserId());
	}

}
