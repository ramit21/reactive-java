import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

public class HotColdObservable {
	
	public static void main(String[] args) {
		createColdObservable();
		createHotConnectableObservable();
	}

	private static void createColdObservable() {
		Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5);
		//Both subscribers get their streams when they ask for it
		observable.subscribe(item-> System.out.println("Observer 1 " + item));
		pause(3000);
		observable.subscribe(item-> System.out.println("Observer 2" + item));
	}

	private static void createHotConnectableObservable() {
		System.out.println("Connectable hot observable:");
		ConnectableObservable<Integer> observable = Observable.just(1, 2, 3, 4, 5).publish();
		
		observable.subscribe(item-> System.out.println("Observable 1 " + item));
		observable.subscribe(item-> System.out.println("Observable 2 " + item));
		
		observable.connect(); //Start transmission now, now connecteble becomes hot observer
		
		//Observer 3 below misses the hot observable, and doesn't print anything
		observable.subscribe(item-> System.out.println("Observable 3 " + item));
	}
	
	public static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
