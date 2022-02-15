package com.example.demo.service;

import java.util.List;

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

}
