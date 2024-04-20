# Maritime Simulation System

## Overview

The Maritime Simulation System is a complex, dynamic simulation environment that integrates port logistics, ship navigation, and resource management. This system leverages modular programming and various design patterns, ensuring flexibility and scalability. It supports a controlled navigation system via tunnels, resource-driven ship construction, and multi-port operations.

### Features

- **Ports and Ships**: Ports function as logistical hubs with capabilities for ship generation, resource management, and unloading processes. Each port can accommodate ships of varying sizes (small, medium, large) and manage different types of resources (gold, wood, meat).
- **Tunnels**: Tunnels restrict ship movement, allowing only five ships at a time, which introduces strategic navigation challenges.
- **Resource Management**: Ports and ships handle essential resources that are crucial for constructing new ships and supporting other operational activities.
- **Factories and Multifactories**: These facilities are responsible for producing ships, resources, and other infrastructure components necessary for port operations. Multifactories enhance production capabilities and efficiency by supporting multiple factories.

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- Maven for dependency management

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/maritime-simulation.git
   ```
2. Navigate to the project directory:
   ```bash
   cd maritime-simulation
   ```
3. Compile the project:
   ```bash
   mvn compile
   ```

### Running the Simulation

To start the simulation, execute the following command in the terminal:
```bash
mvn exec:java -Dexec.mainClass="com.yourpackage.Main"
```

## Documentation

For more detailed documentation, refer to the `docs` folder in the repository.

## Contributing

Contributions are welcome! Please read `CONTRIBUTING.md` for details on our code of conduct, and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.

## Acknowledgments

- Thanks to all contributors who have helped in shaping this project.
- Special thanks to the community for their continuous support and feedback.
