import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JDialog{
    private JTextField tFname;
    private JTextField tFphone;
    private JButton registerButton;
    private JButton cancelButton;
    private JPanel RegisterPanel;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;

    public RegistrationForm(JFrame parent)
    {
        super(parent);
        setTitle("Create a new account");
        setContentPane(RegisterPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String name= tFname.getText();
        String phone=tFphone.getText();
        String password=String.valueOf(passwordField1.getPassword());
        String confirmPassword=String.valueOf(passwordField2.getPassword());

        if(name.isEmpty() || phone.isEmpty() || password.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
        {
            JOptionPane.showMessageDialog(this,
                    "please enter all fields",
                    "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!password.equals(confirmPassword))
        {
            JOptionPane.showMessageDialog(this,
                    "Password doesn't match with confirm password",
                    "Try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        user= addUserToDatabase(name,phone,password);
        if(user!=null)
        {
            dispose();
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",JOptionPane.ERROR_MESSAGE);
        }
    }
    public User user;
    private User addUserToDatabase(String name, String phone, String password) {
        User user=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demodb", "root", "kingkong@123");
            Statement statement = connection.createStatement();
            String queryToExecute = String.format(
                    "insert into users  (name,phone,password) values ('%s', '%s','%s')",
                    name,phone,password);
            int update = statement.executeUpdate(queryToExecute);
            ResultSet rs=statement.executeQuery("select * from users");
            while(rs.next())
            {
                System.out.println(rs.getString("name"));


            }
//            PreparedStatement preparedStatement=connection.prepareStatement(queryToExecute);
//            preparedStatement.setString(1,name);
//            preparedStatement.setString(1,phone);
//            preparedStatement.setString(1,password);

//            int addedRows=preparedStatement.executeUpdate();
            if(update>0) {
                user = new User();
                user.name = name;
                user.phone = phone;
                user.password = password;
            }
            statement.close();
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String args[])
    {
        RegistrationForm myForm = new RegistrationForm(null);
        User user=myForm.user;
        if(user!=null)
        {
            System.out.println("Successfull registration of: "+user.name);
        }
        else
        {
            System.out.println("Registration cancelled");
        }
    }
}


