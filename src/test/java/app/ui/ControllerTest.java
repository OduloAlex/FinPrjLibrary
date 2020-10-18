package app.ui;

import app.dao.CatalogObjDao;
import app.dao.DBException;
import app.dao.PublishingDao;
import app.dao.UserDao;
import app.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest  extends Mockito {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    HttpSession session;
    @Mock
    RequestDispatcher rd;
    @Mock
    List<Card> cardsItems;
    @Mock
    List<CatalogObj> catalogItems;
    @Mock
    List<Order> ordersItems;
    @Mock
    List<User> usersItems;
    @Mock
    List<Author> authorItems;
    @Mock
    List<Publishing> publishingItems;

    @InjectMocks
    Controller controller;

    @Test
    public void doGetLogin() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("login");
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostLogin() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("login");
        when(request.getParameter("localeToSet")).thenReturn("en");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

//    ------------------------------

    @Test
    public void doGetRegistration() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("registration");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostRegistrationLocale() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("registration");
        when(request.getParameter("localeToSet")).thenReturn("en");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void doPostRegistration() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("registration");
        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("description")).thenReturn("");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetLogout() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("logout");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostLogout() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("logout");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

//    ------------------------------

    @Test
    public void doGetError() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("error");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostError() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("error");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

//    ------------------------------

    @Test
    public void doGetListCards() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listCards");
        when(session.getAttribute("cardsItems")).thenReturn(cardsItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doGetListCardsPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listCards");
        when(session.getAttribute("cardsItems")).thenReturn(cardsItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListCards() throws ServletException, IOException, SQLException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listCards");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListCatalog() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listCatalog");
        when(session.getAttribute("catalogItems")).thenReturn(catalogItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doGetListCatalogPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listCatalog");
        when(session.getAttribute("catalogItems")).thenReturn(catalogItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListCatalog() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listCatalog");
        when(request.getParameter("itemId")).thenReturn("a");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListOrders() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listOrders");
        when(session.getAttribute("ordersItems")).thenReturn(ordersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListOrdersPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listOrders");
        when(session.getAttribute("ordersItems")).thenReturn(ordersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListOrders() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listOrders");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostListOrdersCancelId() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listOrders");
        when(request.getParameter("cancelId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListLibReaders() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibReaders");
        when(session.getAttribute("usersItems")).thenReturn(usersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListLibReadersPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibReaders");
        when(session.getAttribute("usersItems")).thenReturn(usersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListLibReadersItemIdOrders() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibReaders");
        when(request.getParameter("itemIdOrders")).thenReturn("a");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListLibReadersItemIdCards() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibReaders");
        when(request.getParameter("itemIdCards")).thenReturn("a");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListLibReaders() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibReaders");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListLibOrders() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibOrders");
        when(session.getAttribute("ordersItems")).thenReturn(ordersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doGetListLibOrdersPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibOrders");
        when(session.getAttribute("ordersItems")).thenReturn(ordersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListLibOrders() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibOrders");
        when(request.getParameter("cancelId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListLibCards() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibCards");
        when(session.getAttribute("cardsItems")).thenReturn(cardsItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListLibCardsPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibCards");
        when(session.getAttribute("cardsItems")).thenReturn(cardsItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListLibCards() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibCards");
        when(request.getParameter("deleteId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetMakeLibCard() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("makeLibCard");
        when(request.getParameter("giveId")).thenReturn("a");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostMakeLibCard() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("makeLibCard");
        when(request.getParameter("makeId")).thenReturn("1");
        when(request.getParameter("date")).thenReturn("2020-01-01");
        when(request.getParameter("state")).thenReturn("room");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostMakeLibCardUpDate() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("makeLibCard");
        when(request.getParameter("makeId")).thenReturn("a");
        when(request.getParameter("date")).thenReturn("2099-01-01");
        when(request.getParameter("state")).thenReturn("room");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostMakeLibCardExceptionParse() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("makeLibCard");
        when(request.getParameter("makeId")).thenReturn("1");
        when(request.getParameter("date")).thenReturn("a");
        when(request.getParameter("state")).thenReturn("room");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListLibCatalog() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibCatalog");
        when(session.getAttribute("catalogItems")).thenReturn(catalogItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListLibCatalogPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibCatalog");
        when(session.getAttribute("catalogItems")).thenReturn(catalogItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListLibCatalog() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listLibCatalog");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListAdminCatalog() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminCatalog");
        when(session.getAttribute("catalogItems")).thenReturn(catalogItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListAdminCatalogPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminCatalog");
        when(session.getAttribute("catalogItems")).thenReturn(catalogItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListAdminCatalog() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminCatalog");
        when(request.getParameter("cancelId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListAdminUsers() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminUsers");
        when(session.getAttribute("usersItems")).thenReturn(usersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListAdminUsersPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminUsers");
        when(session.getAttribute("usersItems")).thenReturn(usersItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListAdminUsers() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminUsers");
        when(request.getParameter("cancelId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostListAdminUsersStateOffId() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminUsers");
        when(request.getParameter("stateOffId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostListAdminUsersStateOnId() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminUsers");
        when(request.getParameter("stateOnId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostListAdminUsersLibOffId() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminUsers");
        when(request.getParameter("libOffId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostListAdminUsersLibOnId() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminUsers");
        when(request.getParameter("libOnId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListAdminAuthors() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminAuthors");
        when(session.getAttribute("authorItems")).thenReturn(authorItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListAdminAuthorsPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminAuthors");
        when(session.getAttribute("authorItems")).thenReturn(authorItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListAdminAuthors() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminAuthors");
        when(request.getParameter("cancelId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostListAdminAuthorsMakeAuthor() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminAuthors");
        when(request.getParameter("makeAuthor")).thenReturn("");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetEditAdminBook() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("editAdminBook");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostEditAdminBook() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("editAdminBook");
        when(request.getParameter("invNumber")).thenReturn("");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostEditAdminBookCancelId() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("editAdminBook");
        when(request.getParameter("cancelId")).thenReturn("a");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostEditAdminBookInvNumber() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("editAdminBook");
        when(request.getParameter("invNumber")).thenReturn("1");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetSettingsAdminBook() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("settingsAdminBook");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostSettingsAdminBook() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("settingsAdminBook");
        when(request.getParameter("name")).thenReturn("abc");
        when(request.getParameter("authorId")).thenReturn("1");
        when(request.getParameter("publishingId")).thenReturn("1");
        when(request.getParameter("year")).thenReturn("1");
        when(request.getParameter("description")).thenReturn("abc");
        when(request.getParameter("fine")).thenReturn("1");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doPostMakeAdminBook() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("makeAdminBook");
        when(request.getParameter("name")).thenReturn("abc");
        when(request.getParameter("authorId")).thenReturn("1");
        when(request.getParameter("publishingId")).thenReturn("1");
        when(request.getParameter("year")).thenReturn("1");
        when(request.getParameter("description")).thenReturn("abc");
        when(request.getParameter("fine")).thenReturn("abc");
        when(request.getParameter("save")).thenReturn("1");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostMakeAdminBookFine() throws ServletException, IOException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("makeAdminBook");
        when(request.getParameter("name")).thenReturn("abc");
        when(request.getParameter("authorId")).thenReturn("a");
        when(request.getParameter("publishingId")).thenReturn("1");
        when(request.getParameter("year")).thenReturn("2020");
        when(request.getParameter("description")).thenReturn("abc");
        when(request.getParameter("fine")).thenReturn("200");
        when(request.getParameter("save")).thenReturn("1");
        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    //    ------------------------------

    @Test
    public void doGetListAdminPublishings() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminPublishings");
        when(session.getAttribute("publishingItems")).thenReturn(publishingItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("next");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }
    @Test
    public void doGetListAdminPublishingsPrevious() throws IOException, ServletException{
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminPublishings");
        when(session.getAttribute("publishingItems")).thenReturn(publishingItems);
        when(session.getAttribute("page")).thenReturn(1);
        when(request.getParameter("goPage")).thenReturn("previous");
        when(request.getRequestDispatcher(anyString())).thenReturn(rd);

        controller.doGet(request, response);

        verify(rd).forward(request, response);
    }

    @Test
    public void doPostListAdminPublishings() throws ServletException, IOException, DBException, SQLException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminPublishings");
        when(request.getParameter("makePublishing")).thenReturn("");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }
    @Test
    public void doPostListAdminPublishingsCancelId() throws ServletException, IOException, DBException, SQLException {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("command")).thenReturn("listAdminPublishings");
        when(request.getParameter("cancelId")).thenReturn("a");

        controller.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

}