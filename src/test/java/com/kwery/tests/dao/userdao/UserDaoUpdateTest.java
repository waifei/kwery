package com.kwery.tests.dao.userdao;

import com.kwery.dao.UserDao;
import com.kwery.models.User;
import com.kwery.tests.fluentlenium.utils.DbTableAsserter.DbTableAsserterBuilder;
import com.kwery.tests.util.RepoDashDaoTestBase;
import com.kwery.tests.util.TestUtil;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;

import static com.kwery.models.User.TABLE_DASH_REPO_USER;
import static com.kwery.tests.fluentlenium.utils.DbUtil.userDbSetUp;
import static com.kwery.tests.fluentlenium.utils.DbUtil.userTable;
import static com.kwery.tests.util.TestUtil.user;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserDaoUpdateTest extends RepoDashDaoTestBase {
    protected UserDao userDao;
    private User user;

    @Before
    public void setUp() {
        user = user();
        userDbSetUp(user);
        userDao = getInstance(UserDao.class);
    }

    @Test
    public void test() throws Exception {
        User modified = user();
        modified.setId(user.getId());

        DozerBeanMapper mapper = new DozerBeanMapper();
        User expected = mapper.map(modified, User.class);

        long now = System.currentTimeMillis();

        TestUtil.nullifyTimestamps(modified);

        modified = userDao.update(modified);

        expected.setUpdated(modified.getUpdated());
        expected.setCreated(user.getCreated());

        new DbTableAsserterBuilder(TABLE_DASH_REPO_USER, userTable(expected)).build().assertTable();

        assertThat(modified.getUpdated(), greaterThanOrEqualTo(now));
        assertThat(modified.getCreated(), is(user.getCreated()));
    }
}
