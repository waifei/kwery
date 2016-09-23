package services.scheduler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PreparedStatementExecutor {
    public final PreparedStatement preparedStatement;

    @Inject
    public PreparedStatementExecutor(@Assisted PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public Future<ResultSet> execute() {
        return Executors.newFixedThreadPool(1).submit(new Callable<ResultSet>() {
            @Override
            public ResultSet call() throws Exception {
                preparedStatement.executeQuery();
                return preparedStatement.getResultSet();
            }
        });
    }
}
