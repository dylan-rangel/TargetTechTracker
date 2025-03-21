# Tech Tracker

Tech Tracker is an Android application that helps users track and manage sales data in various categories. The app uses local SQLite databases on the device to save and organize sales data for each day. It is designed for individuals and businesses who need a simple yet effective solution for logging and monitoring sales transactions related to technology products and services.

## Features

Tech Tracker offers a user-friendly way to track sales across multiple categories. Below are the main features of the app:

- **Track Sales in Multiple Categories:**
  - **Mobile Sales**: Keep track of mobile phone sales.
  - **Electronic Sales**: Log sales of various electronic products.
  - **Protection Plans**: Track sales related to protection plans for mobile devices or other electronics.
  - **Apple Care**: Record sales of Apple Care plans for Apple devices.
  - **Support Tickets**: Keep a record of support tickets created for customers, tracking their status and related sales.
  - **Prepaids**: Log sales of prepaid plans, such as prepaid mobile services.
  - **Consumer Cellular**: Track sales for consumer cellular services, including new contracts, upgrades, and add-ons.

- **Local SQLite Database:**
  - Each sale is logged in a separate SQLite database on the local device, ensuring that data remains accessible even without an internet connection.
  - The app automatically saves data each day, so no manual entry is required for daily sales tracking.
  
- **Daily Data Entry and Logging:**
  - The app is designed to store and organize sales data on a daily basis, allowing users to track their progress over time.
  - Users can enter data manually, and the app will save it in the corresponding SQLite database.

- **Data Access and Reporting:**
  - Access your sales data by category, and view detailed logs for each day's transactions.
  - Future updates will include exporting data for reporting or analysis.

## Database Structure

Tech Tracker uses SQLite databases to store sales data locally. Each category has its own database with the following structure:

### Database Tables

- **Days Table**: Stores individual sale entries with relevant fields such as:
  - `id`
  - `store_id`
  - `total_mobile`
  - `total_electronics`
  - `total_nmp`
  - `total_pp`
  - `total_service`
  - `total_ac`
  - `total_cc`
  - `sale_date`
  
- **Support Tickets Table**: Stores individual sale entries
  - `id`
  - `mobile_sale`
  - `electronic_sale`
  - `protection_plan`
  - `prepaid`
  - `service_ticket`
  - `apple_care`
  - `consumer_cell`
  - `sale_date`
  - `sale_time`
  - `store_id`

### Data Management

- The app automatically manages the SQLite databases, adding new entries for each day's sales and tracking changes over time.
- Users can manually edit or delete records if necessary.

## Getting Started

### Prerequisites

To install and use Tech Tracker, you'll need:

- An Android device (smartphone or tablet).
- Android version 5.0 or higher (Lollipop or newer).
- The ability to install APKs directly or use the Google Play Store.

### Installation
   
1. **Via APK (manual installation):**
   - Download the APK file from the project's release page.
   - Enable "Install from Unknown Sources" in your Android device settings.
   - Open the APK file and follow the on-screen instructions to install.

### Usage

Once installed, follow these steps to begin using the app:

1. **Launch the app** by tapping on the app icon from your home screen.
2. **Log a new sale**:
   - Select the category of the sale you wish to log (e.g., Mobile Sales, Electronic Sales).
   - Enter details like the amount, date of the sale, and any relevant notes.
   - Tap "Save" to store the sale in the appropriate SQLite database.
   
3. **View your sales data**:
   - Navigate to any category to view the recorded sales data.
   - Data will be organized by date, allowing you to view sales from any given day.
   
4. **Edit or Delete Entries**:
   - Tap on any entry to edit the sale details or delete it if necessary.
   
5. **Export Data (Upcoming Feature)**:
   - Future releases will allow users to export sales data to CSV or Excel files for reporting purposes.

## Technologies Used

- **Android SDK**: The app is built using the Android Software Development Kit, which allows it to run on Android devices.
- **SQLite**: SQLite is used for local database management. Each category has a separate database to keep data organized.
- **Kotlin/Java**: The app is developed using Kotlin (or Java) as the programming language.

## Future Improvements

While Tech Tracker is already capable of tracking and managing sales data, the following features are planned for future releases:

- **Exporting Data**: Users will be able to export their sales data to CSV or Excel for better reporting and analysis.
- **Cloud Syncing**: Sync data across multiple devices to ensure it is always up-to-date.
- **Advanced Reporting**: Provide insights and visualizations (e.g., charts, graphs) of sales trends over time.
- **User Authentication**: Allow users to create accounts and sign in to access their data from multiple devices.

## Contributing

We welcome contributions from the community! Here’s how you can help improve Tech Tracker:

1. **Fork the repository** to create your own copy of the project.
2. **Report bugs** or request new features by opening an issue.
3. **Submit a pull request** with your changes or improvements.
4. **Improve documentation**: If you have any suggestions for the README or other documentation, feel free to edit and submit them.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for more details.
