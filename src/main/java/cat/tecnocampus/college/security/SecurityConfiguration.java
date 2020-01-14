package cat.tecnocampus.college.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private DataSource dataSource;

    private static final String USERS_QUERY = "select email, password, enabled from myuser where email = ?";

    private static final String AUTHORITIES_QUERY = "select email, role from authorities where email = ?";

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
    TODO 1: This bean should create a password encoder. The one of security version 5, in which the generated password
     follows the sintax {nam of encoder}password
     For example {noop}pepe   is saying that the password is not encripted and the password is pepe
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*
    TODO 2: You need to modify the following security configuration so that
        * every body can perform (even not logged in users)  "/subjectList", "/registerStudent",
        * only admins and lecturers can perform "/studentList"
        * "/lastRegistration", "/academicRecord", "/registerSubjects" can only be performed by students
        * "/markStudents" can only be perfomred by lecturers
        * "/registerLecturer" can only be performed by admins
        * "/lecturerList" can be performed by authenticated users
     Recall from the readme file that the system has three defined roles: ADMIN, STUDENT and LECTURER
     Note that the current definition contains a line that may make the whole security configuration useless
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/*").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/style/**").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/studentList").hasAnyRole("ADMIN", "LECTURER")
                .antMatchers("/subjectList", "/").permitAll()
                .antMatchers("/lastRegistration").hasRole("STUDENT")
                .antMatchers("/academicRecord").hasRole("STUDENT")
                .antMatchers("/registerSubjects").hasRole("STUDENT")
                .antMatchers("/registerStudent").permitAll()
                .antMatchers("/registerLecturer").hasRole("ADMIN")
                .antMatchers("/markStudents").hasRole("LECTURER")
                .antMatchers("/lecturerList").authenticated()
                .anyRequest().authenticated()

                .and()
                .formLogin() //to use forms (web)

                .and()
                .httpBasic() //to use http headers REST
                .and()

                .rememberMe()
                .tokenValiditySeconds(2419200)
                .key("tecnocampus")

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //needed only when csrf is enabled (as by default is post)
                .logoutSuccessUrl("/"); //where to go when logout is successful

        http
                .csrf().disable()
                .headers()
                .frameOptions().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_QUERY);
    }

}
