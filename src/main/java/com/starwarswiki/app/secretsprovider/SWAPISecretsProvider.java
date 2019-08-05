package com.starwarswiki.app.secretsprovider;

public class SWAPISecretsProvider {

    public static final String API_BASE_URL = "https://swapi.co/api";

    // as of now there are no keys needed,
    // however, later on if there are any API key requirements
    // we can enhance this class(es) to store such secrets and configuration in something like Vault
    // and we can use this class(es)/package to fetch them.

}
