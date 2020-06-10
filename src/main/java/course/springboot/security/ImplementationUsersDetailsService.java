package course.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import course.springboot.model.LoginUsers;
import course.springboot.repository.LoginUsersRepository;

@Service
@Transactional
public class ImplementationUsersDetailsService  implements UserDetailsService {
	
	@Autowired
	private LoginUsersRepository loginUsersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginUsers loginUsers = loginUsersRepository.findUsersByUsername(username);
		
		if (loginUsers == null) {
			throw new UsernameNotFoundException("Username not found");
		}
		
		return new User(loginUsers.getUsername(),
				loginUsers.getPassword(), loginUsers.isEnabled(), 
				true, true, 
				true, loginUsers.getAuthorities());
	}

}
