package pgp.logs.tasks;

public interface ITaskListener<T> {
    void onTaskCompleted(final T returnValue);
    void onTaskCancelled();
}
