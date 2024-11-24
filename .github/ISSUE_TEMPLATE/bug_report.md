---
name: "Bug Report"
about: "Report a bug or issue in the project."
title: "[BUG] "
labels:
  - bug
body:
  - type: markdown
    attributes:
      value: |
        **Please provide the necessary details to help us understand and resolve the issue.**

  - type: textarea
    id: description
    attributes:
      label: "Bug Description"
      description: "Provide a clear and concise description of the bug."
      placeholder: "Describe the bug in detail."

  - type: textarea
    id: steps
    attributes:
      label: "Steps to Reproduce"
      description: "Provide a step-by-step guide to reproduce the bug."
      placeholder: "1. Go to...\n2. Click on...\n3. See the error..."

  - type: input
    id: expected
    attributes:
      label: "Expected Behavior"
      description: "Describe what you expected to happen instead of the bug."
      placeholder: "Describe the expected behavior."

  - type: input
    id: actual
    attributes:
      label: "Actual Behavior"
      description: "Describe what actually happened."
      placeholder: "Describe what happened instead."

  - type: textarea
    id: environment
    attributes:
      label: "Environment"
      description: "Specify the environment where the issue occurred (e.g., OS, browser, version)."
      placeholder: "e.g., Windows 11, Chrome 114, Java 17."

  - type: textarea
    id: additional
    attributes:
      label: "Additional Context"
      description: "Add any other context or screenshots that might help us understand the issue."
      placeholder: "Include logs, screenshots, or other supporting materials."
---
