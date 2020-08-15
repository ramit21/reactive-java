import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class ObservableCreation {
	
	public static void main(String[] args) {
		//3 ways to create Observable
		createUsingJust();
		createUsingIterable();
		createUsingCreate();
		
		createFlowable();
		convertObservableToFlowable();
		
		//Empty vs Never observable
		createEmptyObservable();
		createNeverObservable();
		
		//Lazy using Callable
		createLazyCallable();
	}
	
	private static void createUsingJust(){
		System.out.println("Creating using Just");
		Observable.just(1, 2, 3, 4, 5)
					.subscribe(System.out::println);
	}
	
	private static void createUsingIterable() {
		System.out.println("Creating using Iterable");
		List<Integer> list = Arrays.asList(1,2,3,4,5);
		Observable<Integer> observable= Observable.fromIterable(list);
		observable.subscribe(System.out::println);
	}
	
	private static void createUsingCreate() {
		System.out.println("Creating using Create");
		Observable<Integer> observable = Observable.create(emmiter -> {
			emmiter.onNext(1);
			emmiter.onNext(2);
			emmiter.onNext(3);
			emmiter.onNext(4);
			emmiter.onNext(5);
			emmiter.onComplete(); //close emmision
		});
		observable.subscribe(System.out::println);
	}
	
	private static void createFlowable() {
		System.out.println("Creating a Flowable");
		Flowable<Integer> flowable = Flowable.create(emmiter -> {
			emmiter.onNext(1);
			emmiter.onNext(2);
			emmiter.onNext(3);
			emmiter.onComplete();
		}, BackpressureStrategy.BUFFER);
		flowable.subscribe(System.out::println);
	}
	
	private static void convertObservableToFlowable() {
		System.out.println("Converting Observable to Flowable");
		Observable<Integer> observable = Observable.create(emmiter -> {
			emmiter.onNext(1);
			emmiter.onNext(2);
			emmiter.onNext(3);
			emmiter.onNext(4);
			emmiter.onNext(5);
			emmiter.onComplete(); //close emmision
		});
		observable.toFlowable(BackpressureStrategy.LATEST).subscribe(System.out::println);
	}
	
	private static void createEmptyObservable() {
		//Sends no data, just onComplete notification
		Observable<?> observable = Observable.empty();
		observable.subscribe(System.out::println,
				System.out::println,
				() -> System.out.println("Empty Completed"));
	}

	private static void createNeverObservable() {
		//Neither sends data, nor any notification
		Observable<?> observable = Observable.never();
		observable.subscribe(System.out::println,
				System.out::println,
				() -> System.out.println("doesnt print"));
	}
	
	private static void createLazyCallable() {
		//Using fromCallable, you can create a lazy observable where the getRuntimeException function is invoked
		//only at the time of subscription.
		Observable<Integer> observable = Observable.fromCallable(() -> {return getRuntimeException();});
		observable.subscribe(System.out::println, 
				error-> System.out.println("Error occured: "+error.getLocalizedMessage()));
		
		//In the code below, code breaks at java level, and never reaches the error channel of observer
		//because the Observable is invoked eagerly, even before observer start listening
		observable = Observable.just(getRuntimeException());
		observable.subscribe(System.out::println, 
				error-> System.out.println("This not printed: "+error.getLocalizedMessage()));
	}
	
	private static int getRuntimeException(){
		return 1/0;
	}
}
