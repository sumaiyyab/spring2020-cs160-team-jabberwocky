describe('Login', () => {
    beforeEach(() => {
      cy.visit('/login')
    })
  
    it('accepts username input', () => {
        const input = "email@gmail.com"
        cy.get('#username-input')
          .type(input)
          .should('have.value', input)
      })

      it('accepts password input', () => {
        const input = "test"
        cy.get('#password-input')
          .type(input)
          .should('have.value', input)
      })

      it('login page div elements', () => {
        cy.get('div').should('be.visible');
      })

      it('Allows you to login', () => {
        const username="joe"
        const pw = "123";
        cy.visit('/login')
        cy.get('#username-input').type(username)
        cy.get('#password-input').type(pw)
        cy.get('#button-1').click()
		cy.url().should('eq', 'http://localhost:3000/dashboard')
      
    })

    it('Redirect to the signup page', () => {
      cy.visit('/login')
      cy.get('#link-1').click()
  cy.url().should('eq', 'http://localhost:3000/signup')
    
  })

    it('login with wrong username password', () => {
      const username="moe"
      const pw = "12443";
      cy.visit('/login')
      cy.get('#username-input').type(username)
      cy.get('#password-input').type(pw)
      cy.get('#button-1').click()
      .then(() => "Invalid username or password" )
    
    
  })
   
  })