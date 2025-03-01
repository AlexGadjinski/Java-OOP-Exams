package restaurant.models.working;

import restaurant.models.client.Client;
import restaurant.models.waiter.Waiter;

import java.util.Collection;

public class WorkingImpl implements Working {


    @Override
    public void takingOrders(Client client, Collection<Waiter> waiters) {

        Collection<String> orders = client.getClientOrders();

        // Докато сервитьорът може да работи
        // 1. Вземам поръчка от клиента
        // 2. Добавям поръчката към списъка на сервитьора
        // 3. Премахвам поръчката от списъка на клиента
        // 4. Намалям efficiency-то на сервитьора

        for (Waiter waiter : waiters) {
            while (waiter.canWork() && orders.iterator().hasNext()) {
                String order = orders.iterator().next();
                waiter.takenOrders().getOrdersList().add(order);
                orders.remove(order);
                waiter.work();
            }
        }
    }
}
