//package service;
//
//import org.easymock.EasyMock;
//import org.easymock.EasyMockRule;
//import org.easymock.EasyMockSupport;
//import org.easymock.Mock;
//import org.easymock.TestSubject;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import domain.Offer;
//import domain.Request;
//import domain.UserInfo;
//import domain.UserType;
//import jdbc.OfferJdbc;
//import jdbc.OfferJdbcImpl;
//
//import static org.junit.Assert.*;
//
//public class OfferServiceImplTest extends EasyMockSupport {
//
//    @Rule
//    public EasyMockRule rule = new EasyMockRule(this);
//
//    @Mock
//    private OfferJdbc offerJdbc = new OfferJdbcImpl();
//
//    @TestSubject
//    private OfferServiceImpl offerService = new OfferServiceImpl();
//
//    private UserInfo userInfo;
//
//    @Before
//    public void setup() {
//        userInfo = new UserInfo("rcerveny@iastate.edu", "password", 42, "Ryan", "Cerveny",
//                "venmo","description", UserType.DRIVER, (float) 5.0,
//                new ArrayList<Offer>(), new ArrayList<Request>());
//    }
//
//    @Test
//    public void testGetOfferRequests_passenger() {
//        userInfo.setUserType(UserType.PASSENGER);
//        Offer offer = new Offer(UserType.PASSENGER, 20, userInfo.getNetID(), "destination", "description", "date");
//        List<Offer> offerList = new ArrayList<>();
//        offerList.add(offer);
//        offerList.add(offer);
//
//        EasyMock.expect(offerJdbc.getPassengerOffers()).andReturn(offerList);
//        replayAll();
//        offerService.getOfferRequests(userInfo);
//        verifyAll();
//    }
//
//    @Test
//    public void testGetOfferRequests_driver() {
//        userInfo.setUserType(UserType.DRIVER);
//        Offer offer = new Offer(UserType.DRIVER, 20, userInfo.getNetID(), "destination", "description", "date");
//        List<Offer> offerList = new ArrayList<>();
//        offerList.add(offer);
//        offerList.add(offer);
//
//        EasyMock.expect(offerJdbc.getDriverOffers()).andReturn(offerList);
//        replayAll();
//        offerService.getOfferRequests(userInfo);
//        verifyAll();
//    }
//}