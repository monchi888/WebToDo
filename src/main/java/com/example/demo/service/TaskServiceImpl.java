package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskDao;

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskDao dao;

	public TaskServiceImpl(TaskDao dao) {
		this.dao = dao;
	}

	@Override
	public List<Task> findAll() {
		return dao.findAll();
	}

	@Override
	public Optional<Task> getTask(int id) {

		//削除してください
		Optional<Task> taskOpt = null;
		return taskOpt;

		//Optional<Task>一件を取得 idが無ければ例外発生　

	}

	@Override
	public void insert(Task task) {
		dao.insert(task);
	}

	@Override
	public void update(Task task) {

		//Taskを更新　idが無ければ例外発生

	}

	@Override
	public void deleteById(int id) {

		//Taskを更新 idがなければ例外発生

	}

	@Override
	public List<Task> findByType(int typeId) {
		//2-3 typeIdを引数に指定してdaoのfindByType実行し、結果をreturnする
		return null;
	}
}
