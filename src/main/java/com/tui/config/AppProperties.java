package com.tui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "app.githup")
public class AppProperties {
    private final Github github = new Github();

    public static class Github {
        private String username;
        private String token;
        private String url;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
        
        
    }

    public Github getGithub() {
        return github;
    }

	
}
