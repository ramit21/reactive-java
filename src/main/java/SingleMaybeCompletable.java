import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;

public class SingleMaybeCompletable {

	public static void main(String[] args) {
		createSingle();
		createMaybe();
		createCompletable();
	}

	private static void createSingle() {
		//Takes single argument, and emits a single data message, no completion message
		Single.just("Signle notification").subscribe(System.out::println);
	}

	private static void createMaybe() {
		//empty() so that no item is emitted, but completion is emitted for sure
		Maybe.empty().subscribe(new MaybeObserver() {

			@Override
			public void onSubscribe(@NonNull Disposable d) {
				
			}

			@Override
			public void onSuccess(@NonNull Object t) {
				System.out.println(t);
			}

			@Override
			public void onError(@NonNull Throwable e) {
				
			}

			@Override
			public void onComplete() {
				System.out.println("Maybe onComplete!");
			}
		});
	}
	
	private static void createCompletable() {
		//Completable just completes, doesn't emit anything
		Completable.fromSingle(Single.just("Doesn't get emmited")).subscribe(()->System.out.println("Completable completes"));
	}
}
