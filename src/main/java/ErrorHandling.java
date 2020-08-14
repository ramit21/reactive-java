import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;

public class ErrorHandling {
	
	public static void main(String[] args) {
		throwException();
		throwExceptionUsingCallable();
		
		onErrorResumeWith();
		onErrorReturn();
		
		retryWithPredicate();
		retryWithCount();
		retryUntil();
	}

	private static void throwException() {
		Observable<?> observable = Observable.error(new Exception("Some Exception"));
		
		observable.subscribe(System.out::println,
				error -> System.out.println("Error 1 = "+error.hashCode()));
		observable.subscribe(System.out::println,
				error -> System.out.println("Error 2 = "+error.hashCode()));
	}

	private static void throwExceptionUsingCallable() {
		Observable<?> observable = Observable.error(() -> {
				return new Exception("some ex");
			});
		//When you pass a lambda, a new error instance is sent to each subscriber
		observable.subscribe(System.out::println,
				error -> System.out.println("Error 1 = "+error.hashCode()));
		observable.subscribe(System.out::println,
				error -> System.out.println("Error 2 = "+error.hashCode()));
	}
	
	private static void onErrorResumeWith() {
		//Resume processing despite error on the error channel
			System.out.println("Consume the error gracefully");
			Observable<Integer> observable = Observable.create(emmiter -> {
				emmiter.onNext(1);
				emmiter.onNext(2);
				emmiter.onNext(3/0); //Emit an exception
				emmiter.onNext(4);
				emmiter.onNext(5);
				emmiter.onComplete();
			});
			observable
			      .onErrorResumeWith(Observable.just(10, 20, 30))
			      .subscribe(System.out::println,
					error -> {System.out.println("Error occured: "+error.getLocalizedMessage());});
	}
	
	private static void onErrorReturn() {
		Observable.error(() -> {
				return new Exception("some ex"); //Replace this with IOException, and see different o/p
			})
		    .onErrorReturn(error -> {
		    	if(error instanceof IOException) return 0;
		    	else throw new Exception("this is an Exception");
		    })
			.subscribe(System.out::println,
				error -> System.out.println("Error : "+error.getLocalizedMessage()));
	}
	
	private static void retryWithPredicate() {
		Observable.error(() -> {
			return new Exception("some ex"); //Replace this with IOException, and see infinite loop as it keeps retrying
		})
	    .retry(error -> {
	    	if(error instanceof IOException){
	    		System.out.println("Retrying");
	    		return true;
	    	}
	    	else throw new Exception("this is an Exception");
	    })
		.subscribe(System.out::println,
			error -> System.out.println("Error : "+error.getLocalizedMessage()));
	}
	
	private static void retryWithCount() {
		Observable.error(() -> {
			return new Exception("some ex");
		})
	    .retry(3) //retry 3 times -> very commonly used when calling an API.
		.subscribe(System.out::println,
			error -> System.out.println("Error : "+error.getLocalizedMessage()));
	}
	
	private static void retryUntil() {
		AtomicInteger atomicInteger = new AtomicInteger();
		Observable.error(() -> {
			return new Exception("some ex");
		})
		.doOnError(error -> {
			System.out.println(atomicInteger.get());
			atomicInteger.getAndIncrement();
		})
	    .retryUntil(() -> {
	    	System.out.println("Retrying until...");
	    	return atomicInteger.get() >=3;
	    }) 
		.subscribe(System.out::println,
			error -> System.out.println("Error : "+error.getLocalizedMessage()));
	}


}
