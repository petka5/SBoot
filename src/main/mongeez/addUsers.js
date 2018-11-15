//mongeez formatted javascript
//changeset mlysaght:ChangeSet-1

// $2y$04$KxRbZZhmfOh2E6cMvJDsjOPEcES80FnscKe17wEzV32obesWE/bBy  =  petka
db.users.insert({
    "username" : "petka",
    "password" : "$2a$04$rbSMkcj01sRE8cnQtMvmReYuc9rML6I9qeCV4Wv0NNfOlnVNpMVbK",
    "status" : "ENABLED",
    "createdDate" : ISODate(),
    "lastModifiedDate" : ISODate()
});


db.clientDetails.insert({
	"clientId": "petka-client",
	"secretRequired": false,
	"clientSecret": "$2a$04$rbSMkcj01sRE8cnQtMvmReYuc9rML6I9qeCV4Wv0NNfOlnVNpMVbK",
	"scoped": false,
	"scope": ["read","write"],
	"authorizedGrantTypes": ["refresh_token","implicit","password",	"authorization_code"],
	"authorities": [{
		"role": "admin",
		"_class": "org.springframework.security.core.authority.SimpleGrantedAuthority"
	}],
	"accessTokenValiditySeconds": 3600,
	"refreshTokenValiditySeconds": 21600,
	"autoApprove": false,
	"_class": "org.petka.sboot.persistence.documents.CustomClientDetails"
});