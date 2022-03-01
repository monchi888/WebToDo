package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskDao;

// JUnit5にMockitoという拡張機能を統合する
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskServiceImplの単体テスト")
class TaskServiceImplUnitTest {
	// モック(stub)クラス：ダミーオブジェクト
	@Mock
    private TaskDao dao;

	// テスト対象クラス:モックを探しnewする
	@InjectMocks
    private TaskServiceImpl taskServiceImpl;

	@Test	// テストケース
    // テスト名
    @DisplayName("テーブルtaskの全件取得で0件の場合のテスト")
    void testFindAllReturnEmptyList() {
    	//空のリスト
    	List<Task> list = new ArrayList<>();

        // モッククラスのI/Oをセット（findAll()の型と異なる戻り値はNG）
    	// when(モックのメソッド).thenReturn(想定される戻り値)
    	when(dao.findAll()).thenReturn(list);

        // サービスを実行
        List<Task> actualList= taskServiceImpl.findAll();

        // モックの指定メソッドの実行回数を検査
        // daoが1度だけ呼び出されたことをチェックする
        verify(dao, times(1)).findAll();

        // 戻り値の検査(expected, actual)
        Assertions.assertEquals(0, actualList.size());
    }

    @Test // テストケース
    // テスト名
    @DisplayName("テーブルTaskの全件取得で3件の場合のテスト")
    void testFindAllReturnList() {
    	// モックから返すListに3つのTaskオブジェクトをセット
    	List<Task> list = new ArrayList<>();
    	Task task1 = new Task();
    	Task task2 = new Task();
    	Task task3 = new Task();
    	list.add(task1);
    	list.add(task2);
    	list.add(task3);

        // モッククラスのI/Oをセット（findAll()の型と異なる戻り値はNG）
        when(dao.findAll()).thenReturn(list);

        // サービスを実行
        List<Task> actualList= taskServiceImpl.findAll();

        // モックの指定メソッドの実行回数を検査
        verify(dao, times(1)).findAll();

        // 戻り値の検査(expected, actual)
        Assertions.assertEquals(3, actualList.size());
    }

    @Test // テストケース
    // テスト名
    @DisplayName("タスクが取得できない場合のテスト")
    void testGetTaskThrowException() {
    	/*
    	 * モッククラスのI/Oをセット
    	 * findByIdメソッドでタスクが取得できない場合、
    	 * EmptyResultDataAccessExceptionが1件スローされる
    	 */
        when(dao.findById(0)).thenThrow(new EmptyResultDataAccessException(1));

        // タスクが取得できないとTaskNotFoundExceptionが発生することを検査
        // 詳細はTaskServiceImplを確認するべし
        try {
        	Optional<Task> task0 = taskServiceImpl.getTask(0);
        } catch (TaskNotFoundException e) {
        	assertEquals(e.getMessage(), "指定されたタスクが存在しません");
        }
    }

    @Test // テストケース
    // テスト名
    @DisplayName("タスクを1件取得した場合のテスト")
    void testGetTaskReturnOne() {
    	//Taskをデフォルト値でインスタンス化
    	Task task = new Task();
    	Optional<Task> taskOpt = Optional.ofNullable(task);

        // モッククラスのI/Oをセット
    	when(dao.findById(1)).thenReturn(taskOpt);

        // サービスを実行
    	Optional<Task> taskActual = taskServiceImpl.getTask(1);

        // モックの指定メソッドの実行回数を検査
    	verify(dao, times(1)).findById(1);

        // Taskが存在していることを確認
    	assertTrue(taskActual.isPresent());
    }

    @Test // テストケース(ユニットテストではデータベースの例外は考えない
    // テスト名
    @DisplayName("削除対象が存在しない場合、例外が発生することを確認するテスト")
    void throwNotFoundException() {
        // モッククラスのI/Oをセット
    	when(dao.deleteById(0)).thenReturn(0);

    	//削除対象が存在しない場合、例外が発生することを検査
    	try {
    		taskServiceImpl.deleteById(0);
    	} catch (TaskNotFoundException e) {
    		assertEquals(e.getMessage(), "削除するタスクが存在しません");
    	}
    }
}
