import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class CustomObserver {
	
	/*
	 * Create a custom Observer and pass into subscribe method
	 */
	public static void main(String[] args) {
		Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
		
		Observer<Integer> observer = new Observer<Integer>(){

			@Override
			public void onComplete() {
				System.out.println("Completed!");
			}

			@Override
			public void onError(@NonNull Throwable arg0) {
				
			}

			@Override
			public void onNext(@NonNull Integer arg) {
				System.out.println(arg);
			}

			@Override
			public void onSubscribe(@NonNull Disposable arg0) {
				System.out.println("Suscribed!");
			}
		};
		
		observable.subscribe(observer);
	}
	
}
