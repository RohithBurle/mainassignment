package com.bt.ms.im.entity;

/**
 * @author Suman Mandal
 *
 */
public class RootExceptionDetails {

  private String source;
  private String reasonCode;
  private String reasonText;

  public RootExceptionDetails() {
    super();
  }

  public RootExceptionDetails(String source, String reasonCode, String reasonText) {
    super();
    this.source = source;
    this.reasonCode = reasonCode;
    this.reasonText = reasonText;
  }

  public static RootExceptionDetails of(String source, String reasonCode, String reasonText) {
    return new RootExceptionDetails(source, reasonCode, reasonText);
  }
  /** @return the source */
  public String getSource() {
    return source;
  }
  /** @param source the source to set */
  public void setSource(String source) {
    this.source = source;
  }
  /** @return the reasonCode */
  public String getReasonCode() {
    return reasonCode;
  }
  /** @param reasonCode the reasonCode to set */
  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }
  /** @return the reasonText */
  public String getReasonText() {
    return reasonText;
  }
  /** @param reasonText the reasonText to set */
  public void setReasonText(String reasonText) {
    this.reasonText = reasonText;
  }
}
