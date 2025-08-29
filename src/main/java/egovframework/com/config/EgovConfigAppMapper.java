package egovframework.com.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

/**
 * @ClassName : EgovConfigAppMapper.java
 * @Description : Mapper 설정
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */
@Configuration
@PropertySources({
	@PropertySource("classpath:/application.properties")
})
public class EgovConfigAppMapper {
	@Autowired
	DataSource dataSource;

	@Autowired
	Environment env;

	private String dbType;

	@PostConstruct
	void init() {
		dbType = env.getProperty("Globals.DbType");
	}

	@Bean
	@Lazy
	public DefaultLobHandler lobHandler() {
		return new DefaultLobHandler();
	}

	@Bean(name = {"sqlSession", "egov.sqlSession"})
	public SqlSessionFactoryBean sqlSession() {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);

		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

		sqlSessionFactoryBean.setConfigLocation(
			pathMatchingResourcePatternResolver
				.getResource("classpath:/egovframework/mapper/config/mapper-config.xml"));

		try {
			// 1. 기존 eGovFrame 매퍼 경로 로드
			Resource[] egovMappers = pathMatchingResourcePatternResolver
					.getResources("classpath:/egovframework/mapper/let/**/*_" + dbType + ".xml");

			// 2. 우리가 추가한 매퍼 경로 로드
			Resource[] customMappers = pathMatchingResourcePatternResolver
					.getResources("classpath:/mappers/**/*.xml");

			// 3. 두 경로의 리소스 배열을 하나로 합치기
			Resource[] totalMappers = ArrayUtils.addAll(egovMappers, customMappers);

			// 4. 합친 배열을 최종 매퍼 위치로 설정
			sqlSessionFactoryBean.setMapperLocations(totalMappers);
		} catch (IOException e) {
			// TODO Exception 처리 필요
		}

		return sqlSessionFactoryBean;
	}

	@Bean
	public SqlSessionTemplate egovSqlSessionTemplate(@Qualifier("sqlSession") SqlSessionFactory sqlSession) {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSession);
		return sqlSessionTemplate;
	}
}
