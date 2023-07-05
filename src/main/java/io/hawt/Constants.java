package io.hawt;

public interface Constants {

    String RAW_DEST_DIR = System.getProperty("user.home") + "/camel-github/raw";

    String JSON = "json";

    String ENHANCED_DEST_DIR = System.getProperty("user.home") + "/camel-github/enhanced";

    String AUTH_TOKEN_KEY = "github.auth.token";
    
    String REPO_OWNER_KEY = "github.repo.owner";

    String REPO_NAME_KEY = "github.repo.name";
    
    String REPO_BRANCH_KEY = "github.repo.branch";
}
