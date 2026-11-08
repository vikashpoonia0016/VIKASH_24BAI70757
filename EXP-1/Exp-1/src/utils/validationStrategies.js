/**
 * Strategy Design Pattern
 * ------------------------
 * Each platform defines its own "strategy" object describing:
 *  - limit: max character count
 *  - validate(content): returns { valid, message }
 *
 * Adding a new platform only requires adding a new entry here —
 * no changes needed anywhere else in the app (Open/Closed Principle).
 */

export const platformStrategies = {
  twitter: {
    label: "Twitter / X",
    limit: 280,
    validate(content) {
      if (!content.trim()) {
        return { valid: false, message: "Post content cannot be empty." };
      }
      if (content.length > 280) {
        return {
          valid: false,
          message: `Exceeds Twitter limit by ${content.length - 280} characters.`,
        };
      }
      return { valid: true, message: "" };
    },
  },

  linkedin: {
    label: "LinkedIn",
    limit: 3000,
    validate(content) {
      if (!content.trim()) {
        return { valid: false, message: "Post content cannot be empty." };
      }
      if (content.length > 3000) {
        return {
          valid: false,
          message: `Exceeds LinkedIn limit by ${content.length - 3000} characters.`,
        };
      }
      return { valid: true, message: "" };
    },
  },

  instagram: {
    label: "Instagram",
    limit: 2200,
    validate(content) {
      if (!content.trim()) {
        return { valid: false, message: "Caption cannot be empty." };
      }
      if (content.length > 2200) {
        return {
          valid: false,
          message: `Exceeds Instagram caption limit by ${content.length - 2200} characters.`,
        };
      }
      const hashtags = content.match(/#[\w]+/g) || [];
      if (hashtags.length > 30) {
        return {
          valid: false,
          message: "Instagram allows a maximum of 30 hashtags.",
        };
      }
      return { valid: true, message: "" };
    },
  },
};

// The "context" that dynamically selects a strategy at runtime.
export function validateForPlatform(platform, content) {
  const strategy = platformStrategies[platform];
  if (!strategy) {
    return { valid: false, message: "Unknown platform selected." };
  }
  return strategy.validate(content);
}

export function getLimit(platform) {
  return platformStrategies[platform]?.limit ?? Infinity;
}

export function getPlatformList() {
  return Object.entries(platformStrategies).map(([key, val]) => ({
    key,
    label: val.label,
    limit: val.limit,
  }));
}
