package com.mailgrub.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "firebase.enabled", havingValue = "true", matchIfMissing = true)
public class FirebaseConfig {

  @Value("${com.mailgrub.firebase.web-api-key:}")
  private String webApiKey;

  @Value("${com.mailgrub.firebase.credentials:}")
  private String explicitCredentialPath;

  @Bean
  FirebaseApp firebaseApp() throws IOException {
    GoogleCredentials credentials;

    if (!explicitCredentialPath.isBlank()) {
      try (FileInputStream in = new FileInputStream(explicitCredentialPath)) {
        credentials = GoogleCredentials.fromStream(in);
      }
    } else {
      credentials = GoogleCredentials.getApplicationDefault();
    }

    FirebaseOptions opts = FirebaseOptions.builder().setCredentials(credentials).build();

    if (FirebaseApp.getApps().isEmpty()) {
      return FirebaseApp.initializeApp(opts);
    }
    return FirebaseApp.getInstance();
  }

  @Bean
  FirebaseAuth firebaseAuth(FirebaseApp app) {
    return FirebaseAuth.getInstance(app);
  }

  public String getWebApiKey() {
    return webApiKey;
  }
}
