package net.iquesoft.project.seed.presentation.view.fragment;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import net.iquesoft.project.seed.R;
import net.iquesoft.project.seed.presentation.presenter.GalleryPresenter;
import net.iquesoft.project.seed.presentation.presenter.adapters.GridViewAdapter;
import net.iquesoft.project.seed.presentation.view.activity.TestFragmentActivity;
import net.iquesoft.project.seed.utils.ToastMaker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import butterknife.BindView;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class GridGalleryFragmentTest {
    @Rule
    public ActivityTestRule<TestFragmentActivity> activityTestRule =
            new ActivityTestRule<>(TestFragmentActivity.class);
    @BindView(R.id.gvGridGallery)
    GridView gridView;

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity();
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new GridGalleryFragment(), null).commit();
    }

    @Test
    public void testControls() throws Exception {
        onView(withId(R.id.gvGridGallery)).check(matches(isDisplayed()));
    }

    @Test
    public void testGallery() throws Exception {
        loadImages();
        for (int i = 0; i < GalleryPresenter.getInstance().getListOfImagesId().size(); i++) {
            onData(anything()).inAdapterView(withId(R.id.gvGridGallery))
                    .atPosition(i)
                    .perform(click());
        }
    }

    private void loadImages() {
        try {
            activityTestRule.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    gridView.setAdapter(new GridViewAdapter(activityTestRule.getActivity(), GalleryPresenter.getInstance().getListOfImagesId()));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ToastMaker.showMessage(activityTestRule.getActivity(), " Position " + position);
                        }
                    });
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}