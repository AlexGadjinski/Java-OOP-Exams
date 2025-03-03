package restaurant.repositories;

import restaurant.models.waiter.Waiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class WaiterRepository implements Repository<Waiter> {
    private Collection<Waiter> waiters;

    public WaiterRepository() {
        this.waiters = new ArrayList<>();
    }

    @Override
    public Collection<Waiter> getCollection() {
        return Collections.unmodifiableCollection(waiters);
    }

    @Override
    public void add(Waiter entity) {
        waiters.add(entity);
    }

    @Override
    public boolean remove(Waiter entity) {
        return waiters.remove(entity);
    }

    @Override
    public Waiter byName(String name) {
        return waiters.stream()
                .filter(w -> w.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
