# Mini-Project: Linked Lists

## Overview

This project involves enhancing the implementation of doubly-linked lists by introducing a circular structure with a dummy node to simplify handling edge cases, and implementing a "fail fast" strategy to manage concurrent modifications by multiple iterators. The goal is to improve the robustness and maintainability of the list operations, ensuring that iterators fail gracefully when the list structure is altered unexpectedly.

## Features

### Circular Linking with Dummy Node

- **Dummy Node**: A non-removable node that exists purely to simplify node operations. It acts as both the head and tail of the list, ensuring that the list is never 'empty'.
- **Circular Linking**: The last node links back to the dummy node and the dummy node links forward to the first actual node, making the list circular. This removes the need for null checks on node navigation and simplifies methods like add and remove.

### Fail Fast Iterators

- **Modification Count**: Implements a modification count that is incremented with each structural change to the list (additions or removals of elements).
- **Iterator Validation**: Each iterator checks the modification count before each operation. If the modification count does not match its expected value, it throws a `ConcurrentModificationException`, ensuring that the user is made aware of unsafe operations.

## Discussion

Incorporating a dummy node along with circular linking in the implementation of the `SimpleCDLL` class has significantly simplified the handling of edge cases, making the code more robust and easier to maintain. Traditionally, doubly-linked lists require multiple checks for boundary conditions, such as inserting or removing elements at the beginning or end of the list, which complicates the logic and increases the risk of errors. By using a dummy node that acts as both the head and tail of the list, we eliminate the need for null checks and special cases for these operations. This node seamlessly integrates into the structure by always existing, thereby simplifying methods like `add`, `remove`, and iterator movements. Every node always has a non-null `next` and `prev`, which means that the same insertion or deletion logic applies uniformly across all nodes, including edge nodes. Circular linking further enhances this by ensuring that the list wraps around to the dummy node, making the list's endpoints indistinguishable from each other in the context of traversal and manipulation. This uniformity not only makes the code cleaner and more concise but also reduces the likelihood of bugs associated with special case handling.

## Acknowledgements

- **Samuel A. Rebelsky**
- **Zakariye Abdilahi**

## Acknowledgements

- **Samuel A. Rebelsky**: For providing the coding in the lab "Linked Lists"
