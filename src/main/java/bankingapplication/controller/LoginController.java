package bankingapplication.controller;


import bankingapplication.dto.JwtRequestDto;
import bankingapplication.dto.JwtResponseDto;
import bankingapplication.exception.BankException;
import bankingapplication.securityconfig.JwtTokenUtil;
import bankingapplication.service.JwtServiceImpl;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class LoginController {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;


  @Autowired
  private JwtServiceImpl.JwtUserDetailsService userDetailsService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<JwtResponseDto> createAuthenticationToken(@RequestBody @Valid JwtRequestDto authenticationRequest) throws Exception {


    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.getUsername());
    if (!Objects.nonNull(userDetails)) {
      throw new BankException(String.format("User not found %s", authenticationRequest.getUsername()), HttpStatus.NO_CONTENT);
    }
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    final String token = jwtTokenUtil.generateToken(userDetails);


    return ResponseEntity.ok(new JwtResponseDto(token));
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }

}
