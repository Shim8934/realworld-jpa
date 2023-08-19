package com.io.realworldjpa.domain.user.entity;

import com.io.realworldjpa.global.util.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.regex.Pattern.matches;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Embeddable
public class Email {

  @Column(name = "email", unique = true)
  private String address;

  public Email(String address) {
    checkArgument(isNotEmpty(address), "address must be provided.");
    checkArgument(
      address.length() >= 4 && address.length() <= 50,
      "address length must be between 4 and 50 characters."
    );
    checkArgument(checkAddress(address), "Invalid email address: " + address);

    this.address = address;
  }

  protected Email() {

  }

  private static boolean checkAddress(String address) {
    return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
  }

  @Generated
  public String getName() {
    String[] tokens = address.split("@");
    if (tokens.length == 2)
      return tokens[0];
    return null;
  }

  @Generated
  public String getDomain() {
    String[] tokens = address.split("@");
    if (tokens.length == 2)
      return tokens[1];
    return null;
  }

  public String getAddress() {
    return address;
  }

  @Override
  @Generated
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Email email = (Email) o;
    return Objects.equals(address, email.address);
  }

  @Override
  @Generated
  public int hashCode() {
    return Objects.hash(address);
  }

  @Override
  @Generated
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("address", address)
      .toString();
  }

}