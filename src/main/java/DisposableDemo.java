import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.ResourceObserver;

public class DisposableDemo {
	
	/*
	 * Disposable are used to gracefully handle the stream 
	 * that is opened when we subscribe to an Observer
	 */
	
	public static void main(String[] args) {
		handleDisposable();
		handleDisposeInObserver();
		handleDisposeOutsideObserver();
		compositeDisposable();
	}

	private static void handleDisposable() {
		Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);//interval emits on periodic basis
		Disposable disposable = observable.subscribe(System.out::println);
		HotColdObservable.pause(3000);
		disposable.dispose();
		System.out.println("Stream disposed");
		HotColdObservable.pause(3000);
	}
	
	private static void handleDisposeInObserver() {
		Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
		Observer<Integer> observer = new Observer<Integer>() {
			
			Disposable d = null;
			
			@Override
			public void onSubscribe(@NonNull Disposable d) {
				// This method has disposable which can be used to dsipose off the stream
				// Capture this in a variable as done here 
				// and dispose at point in time (any of the methods)
				this.d = d;
			}

			@Override
			public void onNext(@NonNull Integer t) {
				System.out.print(t+" ");
				if(t==3){
					d.dispose(); //disposing stream at 3 only, subsequent numbers will not be captured
					System.out.println("\nDisposed inside Observer");
				}
			}

			@Override
			public void onError(@NonNull Throwable e) {
				
			}

			@Override
			public void onComplete() {
				
			}
		}; 
		
		observable.subscribe(observer);
	}
	
	private static void handleDisposeOutsideObserver() {
		Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
		ResourceObserver<Integer> observer = new ResourceObserver<Integer>() {

			@Override
			public void onNext(@NonNull Integer t) {
				System.out.print(t+" ");
			}

			@Override
			public void onError(@NonNull Throwable e) {
				
			}

			@Override
			public void onComplete() {
				
			}
		}; 
		
		Disposable d = observable.subscribeWith(observer);
		d.dispose();
	}
	
	private static void compositeDisposable() {
		Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);
		Disposable disposable1 = observable.subscribe((item) -> System.out.println("Observer 1: "+item));
		Disposable disposable2 = observable.subscribe((item) -> System.out.println("Observer 2: "+item));
		
		CompositeDisposable compositeDisposable = new CompositeDisposable();
		compositeDisposable.addAll(disposable1, disposable2);
		HotColdObservable.pause(3000);
		compositeDisposable.dispose();
		HotColdObservable.pause(3000);
		System.out.println("Composite disposable disposes both.");
	}

}
