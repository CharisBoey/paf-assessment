package vttp2023.batch4.paf.assessment.bedandbreakfastapp.repositories;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp2023.batch4.paf.assessment.bedandbreakfastapp.models.Bookings;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.models.User;
import vttp2023.batch4.paf.assessment.bedandbreakfastapp.services.BookingException;



@Repository
public class BookingsRepository {
	
	// You may add additional dependency injections

	public static final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email like %";

	public static final String SQL_INSERT_NEW_USER = "insert into users values (?,?)";

	public static final String SQL_INSERT_BOOKING_DETAILS = "insert into bookings (booking_id, listing_id, duration, email) values (?,?,?,?)";

	@Autowired
	private JdbcTemplate template;

	// You may use this method in your task
	public Optional<User> userExists(String email) {
		SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, email);
		if (!rs.next())
			return Optional.empty();

		return Optional.of(new User(rs.getString("email"), rs.getString("name")));
	}

	// TODO: Task 6 
	//REQUIRES A TRANSACTION!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! BUT ON SERVICE METHOD
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	/* @Transactional(rollbackFor = {NoSuchFieldError.class, SecurityException.class, IllegalAccessException.class, DataAccessException.class})
	public void newUser(User user) throws NoSuchFieldException, SecurityException, IllegalArgumentException, DataAccessException {
		
        RecordComponent[] rc = User.class.getRecordComponents();      
        Field field=null;
		User.class.getDeclaredField(rc[0].getAccessor().getName());
		try {
			field = User.class.getDeclaredField(rc[0].getAccessor().getName());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        field.setAccessible(true); 
        try {
			System.out.println(field.get(user));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Field field2=null;
		try {
			field2 = User.class.getDeclaredField(rc[1].getAccessor().getName());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        field2.setAccessible(true); 
        try {
			System.out.println(field2.get(user));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			template.update(SQL_INSERT_NEW_USER, field.get(user), field2.get(user));
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} */

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	//booking_id, listing_id, duration, email
	/* public void newBookings(Bookings bookings) {
		template.update(SQL_INSERT_BOOKING_DETAILS, bookings.getBookingId(), bookings.getListingId(), bookings.getDuration(), bookings.getEmail());
	} */

	//TASK 6 ANSWER METHOD 1 ------------------------------------------------------------------------------
	/* public void newUser(User user) throws BookingException{
		//to get exceptions
		//This is slightly weird since you are using BookingException for the creation of a new User --> create a Error specific to this instead of Booking
		try{
			//only one should be updated 
			if (template.update(SQL_INSERT_NEW_USER, user.email(), user.name()) != 1){
				throw new BookingException("Cannot insert");
			}
		} catch (DataAccessException ex){
			throw new BookingException(ex.getMessage());
		}
		
	}

	public void newBookings(Bookings bookings) {
		//Do the same throw exception here
		template.update(SQL_INSERT_BOOKING_DETAILS, bookings.getBookingId(), bookings.getListingId(), bookings.getDuration(), bookings.getEmail());
	} */
	//END ANSWER 1 ----------------------------------------------------------------------------------------

	//TASK 6 ANSWER METHOD 2 ------------------------------------------------------------------------------
	public void newUser(User user) throws BookingException{
		
		if (template.update(SQL_INSERT_NEW_USER, user.email(), user.name()) != 1){
			//needa use the {} for abstract class which is what DataAccessException is
			//throw new DataAccessException("Cannot create new User"){};

			//ORRR

			//Throw ANY runtime exception
			throw new IllegalArgumentException("Cannot create new User");
		}
		
	}

	public void newBookings(Bookings bookings) {
		//Do the same throw exception here
		if(template.update(SQL_INSERT_BOOKING_DETAILS, bookings.getBookingId(), bookings.getListingId(), bookings.getDuration(), bookings.getEmail()) !=1){
			throw new IllegalArgumentException("Cannot create new Booking");
		};
	}
	//END ANSWER 2 ----------------------------------------------------------------------------------------


	//NOTE: Either handle exception in service or repository (might be easier in service) if done in service, just do generic ones in repo and deal with them in service
}
