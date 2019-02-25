package devy.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityApplication.class);

	// Table 생성 스크립트
	private static final String SQL_NAME 	  = "create.sql";

	// SQLite 데이터베이스 파일
	private static final String DB_NAME       = "sqlite.db";

	// SQLite 명령을 실행시키는 도구
	private static final String SQLITE 		  = "sqlite3";

	// 터미널에서 SQLite의 데이터베이스를 생성하는 명령
	private static final String CMD 		  = SQLITE + " " + DB_NAME;

	private static final File FILE_DB 		  = new File(DB_NAME);
	private static final File FILE_CREATE_SQL = new File(SQL_NAME);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	/**
	 * Spring Boot Application가 시작될때 실행할 동작을 정의합니다.
	 * @throws Exception Exception 발생시 모두 출력
	 */
	@Override
	public void run(String... args) throws Exception {
		try {
			// sqlite.db 파일을 생성
			if(!FILE_DB.exists()) {
				Runtime.getRuntime().exec(CMD);
			}

			// create.sql 파일의 Table 생성 스크립트를 읽어드려 String으로 변환
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_CREATE_SQL)));
			StringBuilder sb = new StringBuilder();
			String temp;
			while((temp = br.readLine()) != null) {
				sb.append(temp);
			}

			// 세미콜론(;)을 기준으로 Table 생성 Query를 분리하여 Array로 생성한 후 차례대로 실행
			String[] sqlArr = sb.toString().split(";");
			for(String sql : sqlArr) {
				logger.info("Execute DDL '" + sql + "'");
				this.jdbcTemplate.execute(sql);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
