# Java-Progress-Toolkit
A thread-safe progress reporting framework for Java, providing seamless synchronisation between background tasks and CLI or Swing interfaces

A lightweight, decoupled, and thread-safe progress reporting framework for Java. This toolkit provides a seamless way to execute background tasks while synchronising progress updates with either **Command Line Interfaces (CLI)** or **Swing Graphical Interfaces (GUI)**.

## Key Features
* **Decoupled Architecture**: Separation of business logic (`WorkTask`) from UI implementation.
* **Thread Safety**: Automatic handling of Swing's Event Dispatch Thread (EDT) via `ProgressWorker`.
* **Efficiency**: Built-in update coalescing to prevent console flicker or UI lag.
* **Dual-Interface Support**: Works out-of-the-box for ASCII terminal bars and `JProgressBar`.

## Installation
Clone the repository and include the `progressbar` package in your project:
```bash
git clone https://github.com/trevormaggs/java-progress-toolkit.git
```

## Usage

### 1. Define your Task
Implement the `WorkTask` interface. This code remains exactly the same regardless of your UI.

```java
WorkTask myTask = (listener) -> {
    int total = 100;
    for (int i = 1; i <= total; i++) {
        // Do heavy work...
        listener.onProgressUpdate(i, total);
    }
};
```

### 2. Run in Console
```java
myTask.execute(new ConsoleProgressBar());
```

### 3. Run in Swing GUI
```java
List<ProgressListener> listeners = Collections.singletonList(new SwingProgressAdapter(myJProgressBar));
ProgressWorker worker = new ProgressWorker(myTask, listeners);
worker.execute();
```
---
