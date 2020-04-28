describe('Dashboard', () => {
    beforeEach(() => {
      cy.visit('/login')
        cy.get('#username-input').type('moe')
        cy.get('#password-input').type('123')
        cy.get('#button-1').click()
    })
    it('dashboard page div elements', () => {
        cy.get('div').should('be.visible');
      })
      it('Popup page showing up', () => {
        cy.visit('/dashboard')
        cy.get('#button3').click()
		cy.get('#dialog').should('be.visible')
      
    })
})