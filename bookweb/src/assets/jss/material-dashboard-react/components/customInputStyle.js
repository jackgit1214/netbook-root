import {
  primaryColor,
  dangerColor,
  successColor,
  grayColor,
  defaultFont
} from "assets/jss/material-dashboard-react.js";

const customInputStyle = {
  disabled: {
    "&:before": {
      backgroundColor: "transparent !important"
    }
  },
  underline: {
    "&:hover:not($disabled):before,&:before": {
      borderColor: grayColor[4] + " !important",
      borderWidth: "1px !important"
    },
    "&:after": {
      borderColor: primaryColor[0]
    }
  },
  underlineError: {
    "&:after": {
      borderColor: dangerColor[0]
    }
  },
  underlineSuccess: {
    "&:after": {
      borderColor: successColor[0]
    }
  },
  labelRoot: {
    ...defaultFont,
    color: grayColor[3] + " !important",
    fontWeight: "400",
    fontSize: "14px",
    lineHeight: "0.62857",
    letterSpacing: "unset"
  },
  labelRootError: {
    color: dangerColor[0]
  },
  labelRootSuccess: {
    color: successColor[0]
  },
  feedback: {
    position: "absolute",
    top: "18px",
    right: "0",
    zIndex: "2",
    display: "block",
    width: "24px",
    height: "24px",
    textAlign: "center",
    pointerEvents: "none"
  },
  marginTop: {
    marginTop: "2px",
      marginLeft:8
  },
  formControl: {
    paddingBottom: "2px",
    margin: "2px 0 0 8px",
    position: "relative",
    verticalAlign: "unset"
  }
};

export default customInputStyle;
