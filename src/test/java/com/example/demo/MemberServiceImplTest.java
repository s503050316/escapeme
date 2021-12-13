package com.example.demo;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.Common.RandomNumber;
import com.example.demo.Common.RandomNumberImpl;
import com.example.demo.model.DB.Account;
import com.example.demo.model.DB.Taskform;
import com.example.demo.repository.LikeFunctionRepository;
import com.example.demo.repository.MemberServiceRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.PostingSystemRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.MailService;
import com.example.demo.service.MemberService;
import com.example.demo.service.helper.Sha256Helper;
import com.example.demo.service.helper.Sha256HelperImpl;
import com.example.demo.serviceimpl.MemberServiceImpl;
import com.example.demo.serviceimpl.TaskformServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceImplTest {

	@Mock
	private Sha256Helper _sha256Helper;

	@Mock
	private MemberServiceRepository _memberServiceRepository;

	@Mock
	private PostingSystemRepository _postingSystemRepository;

	@Mock
	private MessageRepository _messageRepository;

	@Mock
	private TaskRepository _taskRepository;

	@Mock
	private MailService _mailService;

	@Mock
	private RandomNumber _randomNumber;

	@Mock
	private LikeFunctionRepository _likeFunctionRepository;

	private MemberService _sut;

	// Select中文字串，從TaskForm表單Content欄位搜尋資訊;
	@BeforeEach
	public void TestInitalize() {
		MockitoAnnotations.initMocks(this);

		// 初始化目標物件
		_sut = new MemberServiceImpl(_sha256Helper, _memberServiceRepository, _postingSystemRepository,
				_messageRepository, _taskRepository, _mailService, _randomNumber, _likeFunctionRepository);

	}

//	@ParameterizedTest
//	@ValueSource(strings = {"", "  "})
//	void isBlank_ShouldReturnTrueForNullOrBlankStrings(String input) {
//	    assertTrue(Strings.isBlank(input));
//	}

	@Test
	public void signUp_輸入已註冊過的信箱_傳回字串信箱重複() {
		// arrange
		/*
		 * - 初始化目標物件 - 初始化方法參數 - 建立模擬物件行為 - 設定環境變數期望結果
		 */

		Account account = new Account();
		account.setEmail("asd990055@gmail.com");
		// 建立模擬物件行為
		when(_memberServiceRepository.findByEmail("asd990055@gmail.com")).thenReturn(account);

		// 設定環境變數期望結果
		String expected = "信箱重複";

		// act
		// 實際呼叫測試目標物件的方法
		String actual = _sut.signUp(account);

		// assert
		// 驗證目標物件是否如同預期運作
		Assertions.assertEquals(expected, actual);
	}
	@Test
	public void signUp_輸入完整訊息_傳回字串完成註冊() {
		// arrange
		/*
		 * - 初始化目標物件 - 初始化方法參數 - 建立模擬物件行為 - 設定環境變數期望結果
		 */

		Account account = new Account();

		
		// 建立模擬物件行為
		when(_memberServiceRepository.findByEmail("asd990055@gmail.com"))
		.thenReturn(account);

		account.setPassword(_sha256Helper.Encryption("21321321"));

		account.setMail("123456");

		when(_memberServiceRepository.save(account)).thenReturn(account);

		_mailService.sendMail("b@gmail.com", "ESCapeMe驗證信",
				"您好，" + "歡迎您加入ESCapeMe這個大家族以下為您的驗證碼" + "『" + "123456" + "』");

		// 設定環境變數期望結果
		String expected = "完成註冊";

		// act
		// 實際呼叫測試目標物件的方法
		String actual = _sut.signUp(account);

		// assert
		// 驗證目標物件是否如同預期運作
		verify(_mailService, times(1)).sendMail("b@gmail.com", "ESCapeMe驗證信",
				"您好，" + "歡迎您加入ESCapeMe這個大家族以下為您的驗證碼" + "『" + "123456" + "』");
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void findByEmailAndPassword_輸入前端傳回的信箱_傳回信箱為空值(){
		// arrange
		/*
		 * - 初始化目標物件 - 初始化方法參數 - 建立模擬物件行為 - 設定環境變數期望結果
		 */

		Account account = new Account();

		// 建立模擬物件行為 // .thenReturn //verify return void
		when(_memberServiceRepository.findByEmail("b@gmail.com")).thenReturn(account);

		account.setEmail(null);
		
		// 設定環境變數期望結果
		Account expected = account;

		// act
		// 實際呼叫測試目標物件的方法
		Account actual = _sut.findByEmailAndPassword(account);

		// assert
		// 驗證目標物件是否如同預期運作
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void findByEmailAndPassword_輸入前端傳回的密碼_傳回密碼為null() {
		// arrange
		/*
		 * - 初始化目標物件 - 初始化方法參數 - 建立模擬物件行為 - 設定環境變數期望結果
		 */

		Account account = new Account();
		account.setEmail("b@gmail.com");
		account.setPassword("");
		// 建立模擬物件行為 
		when(_memberServiceRepository.findByEmail("b@gmail.com")).thenReturn(account);
		
		
		// 設定環境變數期望結果
		Account expected = account;

		// act
		// 實際呼叫測試目標物件的方法
		Account actual = _sut.findByEmailAndPassword(account);

		// assert
		// 驗證目標物件是否如同預期運作
		
		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void findByEmailAndPassword_輸入前端傳回的正確信息_傳回正確的帳戶信息() {
		// arrange
		/*
		 * - 初始化目標物件 - 初始化方法參數 - 建立模擬物件行為 - 設定環境變數期望結果
		 */

		Account account = new Account();
		account.setEmail("b@gmail.com");
		account.setPassword("12345678");
		// 建立模擬物件行為 
		when(_memberServiceRepository.findByEmail("b@gmail.com")).thenReturn(account);
		
		
		// 設定環境變數期望結果
		Account expected = account;

		// act
		// 實際呼叫測試目標物件的方法
		Account actual = _sut.findByEmailAndPassword(account);

		// assert
		// 驗證目標物件是否如同預期運作
		
		Assertions.assertEquals(expected, actual);
	}

}
