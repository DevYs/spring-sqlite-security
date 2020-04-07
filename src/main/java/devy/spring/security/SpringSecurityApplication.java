package devy.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;

@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityApplication.class);

	// SQLite의 Datasource 이름
	private static final String DATASOURCE_NAME_SQLITE = "jdbc/sqlite";

	// DDL SQL script file name
	private static final String DDL_SQL_NAME  = "ddl.sql";

	// DML SQL script file name
	private static final String DML_SQL_NAME  = "dml.sql";

	// SQLite 데이터베이스 파일
	private static final String DB_NAME       = "sqlite.db";

	// SQLite 명령을 실행시키는 도구
	private static final String SQLITE 		  = "sqlite3";

	// 터미널에서 SQLite의 데이터베이스를 생성하는 명령
	private static final String CMD 		  = SQLITE + " " + DB_NAME;

	private static final File FILE_DB 		  = new File(DB_NAME);
	private static final File FILE_DDL_SQL 	  = new File(DDL_SQL_NAME);
	private static final File FILE_DML_SQL 	  = new File(DML_SQL_NAME);

	@Value("${spring.datasource.name}")
	private String datasourceName;

	@Value("${devy.run-sql.ddl}")
	private boolean isRunDDL;

	@Value("${devy.run-sql.dml}")
	private boolean isRunDML;

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

		if(!datasourceName.equals(DATASOURCE_NAME_SQLITE)) {
			return;
		}

		try {
			// sqlite.db 파일을 생성
			if(!FILE_DB.exists()) {
				Runtime.getRuntime().exec(CMD);
			}

			// application.properties 파일의 devy.run-sql.ddl 값이 true라면 ddl sql script를 실행시킨다
			if(isRunDDL) {
				runScript(FILE_DDL_SQL);
			}

			// application.properties 파일의 devy.run-sql.dml 값이 true라면 dml sql script를 실행시킨다
			if(isRunDML) {
				runScript(FILE_DML_SQL);
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void runScript(File sqlFile) throws IOException {
		// sql script 파일의 스크립트를 읽어드려 String으로 변환
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFile)));
		StringBuilder sb = new StringBuilder();
		String temp;
		while((temp = br.readLine()) != null) {
			sb.append(temp);
		}

		// 세미콜론(;)을 기준으로 Query를 분리하여 Array로 생성한 후 차례대로 실행
		// 만약 실행시킬 sql 쿼리가 없다면 continue...
		String[] sqlArr = sb.toString().split(";");
		for(String sql : sqlArr) {
			if(sql.length() == 0) {
				continue;
			}

			logger.info("Execute DDL '" + sql + "'");
			this.jdbcTemplate.execute(sql);
		}
	}

}
