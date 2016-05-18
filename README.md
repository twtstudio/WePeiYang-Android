# WePeiYang-Android
A tools application for Tianjin University.

## Network Module Usage
for example,

API Request: `GET /auth/token/get?twtuname={}&twtpasswd={}`

API Response:

```
{
	"error_code": 1000,
	"message": "message",
	"data": {
		"token": "xxxxxxx"
	}
}
```

Following these steps to add new API:

1. add data model class in package `com.twt.service.model`. Note this, declaring the variable as public.

	```
	public class Token {
		public String token;
	}

	```

2. add HTTP API to class `com.twt.service.api.WePeiYang`.

	```
	@GET("/auth/token/get")
	 Observable<APIResponse<Token>> login(@Query("twtuname")String twtuname, @Query("twtpasswd")String twtpasswd);
	```
3. add public method to class `com.twt.service.api.WePeiYangClient`.

	```
	public void login(Object tag, Subscriber<Token> subscriber, String username, String password) {
		Subscription subscription = mService.login(username, password)
    		.map(new ResponseTransformer<Token>())
	    	.compose(WePeiYangClient.<Token>applySchedulers())
  		  	.subscribe(subscriber);
	   addSubscription(tag, subscription);
  }
	```
	
	
	
	Following these steps to use new API.

1. Add OnNextListener<T> in your working Controller or Presenter, or anywhere you want.

	```
	protected OnNextListener<Token> mOnTokenListener = new OnNextListener<Token>() {
		@Override
		public void onNext(Token token) {
			// do some stuff...
		}
	}
	```

2. Just call the new API method.

	```
	WePeiYangClient.getInstance()
		.login(this, new APISubscriber<>(mContext, mOnTokenListener), twtuname, twtpasswd);

	```


