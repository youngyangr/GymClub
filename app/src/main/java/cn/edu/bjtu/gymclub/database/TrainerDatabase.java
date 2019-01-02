package cn.edu.bjtu.gymclub.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import cn.edu.bjtu.gymclub.interfaces.TrainerDao;
import cn.edu.bjtu.gymclub.model.Trainer;

/**
 * The Room database.
 */
@Database(entities = {Trainer.class}, version = 1)
public abstract class TrainerDatabase extends RoomDatabase {

    /**
     * @return The DAO for the Cheese table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract TrainerDao trainer();

    /** The only instance */
    private static TrainerDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized TrainerDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), TrainerDatabase.class, "ex")
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                TrainerDatabase.class).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty.
     */
    private void populateInitialData() {
        if (trainer().count() == 0) {
            Trainer trainer = new Trainer();
            beginTransaction();
            try {
                for (int i = 0; i < Trainer.TRAINERS.length; i++) {
                    trainer.name = Trainer.TRAINERS[i];
                    trainer().insert(trainer);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

}
